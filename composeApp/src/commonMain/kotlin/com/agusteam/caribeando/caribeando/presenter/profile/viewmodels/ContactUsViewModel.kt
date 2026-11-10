package com.agusteam.caribeando.presenter.profile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.ContactInfoResponse
import com.agusteam.caribeando.data.network.services.OrderService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 2026-10-09: loads the real support contact channels for "Contáctanos" from the backend
 * (GET /support/contact) instead of the design handoff's placeholder phone/email.
 */
class ContactUsViewModel(
    private val orderService: OrderService,
) : ViewModel() {

    data class State(
        val isLoading: Boolean = true,
        val contact: ContactInfoResponse? = null,
        val failed: Boolean = false,
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, failed = false)
            _state.value = when (val result = orderService.getContactInfo()) {
                is OperationResult.Success -> State(isLoading = false, contact = result.data)
                is OperationResult.Error -> State(isLoading = false, failed = true)
            }
        }
    }
}
