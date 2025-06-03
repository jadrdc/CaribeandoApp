package com.agusteam.caribeando.core.base

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.reflect.KProperty1

abstract class GenericViewModel<S : ViewModelState, E : Any>(
    initialState: S
) : ViewModel() {


    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S>
        get() = _state

    val events: Flow<E>
        get() = _events.receiveAsFlow()

    /*
     * Never ever change the channel size, it might lead to unpredicted behavior.
     */
    private val _events = Channel<E>(Channel.UNLIMITED)
    private val stateMutex = Mutex()

    /**
     * Update the View state using a reducer function synchronously.
     *
     */
    protected suspend fun setState(reducer: S.() -> S) {
        _state.value = stateMutex.withLock {
            _state.value.reducer()
        }
    }

    /**
     * Update the View state using a reducer function asynchronously.
     *
     */
    protected fun updateState(reducer: S.() -> S) {
        viewModelScope.launch {
            _state.update(reducer)
        }
    }

    /**
     * Sends an Event to the Event's queue. Each event will be received one time by a single
     * listener.
     *
     */
    protected fun sendEvent(event: E) {
        _events.trySend(event).let {
            if (!it.isSuccess || it.isClosed) {
                throw MviBaseException(
                    "$it",
                    IllegalStateException(
                        "The only known way to fail with this exception is to exceed the " +
                                "capacity of the channel. The capacity is set to UNLIMITED, " +
                                "so there should be no way to fail with this exception."
                    )
                )
            }
        }
    }

    protected fun collectState(
        collector: FlowCollector<S>,
    ) = viewModelScope.launch {
        state.collect(collector)
    }

    protected fun <P> collectStateProperty(
        prop: KProperty1<S, P>,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        collector: FlowCollector<P>,
    ) {
        viewModelScope.launch(dispatcher) {
            state.map { prop.get(it) }
                .distinctUntilChanged()
                .collect(collector)
        }
    }

    protected fun <P1, P2> collectStateProperties(
        prop1: KProperty1<S, P1>,
        prop2: KProperty1<S, P2>,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        collector: FlowCollector<Pair<P1, P2>>,
    ) {
        viewModelScope.launch(dispatcher) {
            state.map { prop1.get(it) to prop2.get(it) }
                .distinctUntilChanged()
                .collect(collector)
        }
    }

    /**
     * Binds provided Flow to update ViewModel state
     */
    protected fun <T> Flow<T>.bind(reducer: S.(value: T) -> S) {
        onEach { value ->
            setState {
                reducer(value)
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Never use it: this function added for test purpose only.
     */
    @VisibleForTesting
    internal fun closeEvents() {
        _events.close()
    }


}

interface ViewModelState

open class MviBaseException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)