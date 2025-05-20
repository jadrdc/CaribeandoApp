package com.agusteam.caribeando.presenter.profile.viewmodels

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.core.base.OperationResult
import com.agusteam.caribeando.data.model.Token
import com.agusteam.caribeando.data.util.EMAIL
import com.agusteam.caribeando.data.util.LAST_NAME
import com.agusteam.caribeando.data.util.NAME
import com.agusteam.caribeando.data.util.PHONE
import com.agusteam.caribeando.domain.models.ErrorModel
import com.agusteam.caribeando.domain.usecase.GetLocalProfileUseCase
import com.agusteam.caribeando.domain.usecase.GetUserProfileUseCase
import com.agusteam.caribeando.domain.usecase.LogoutUseCase
import com.agusteam.caribeando.domain.usecase.SaveLocalDataUseCase
import com.agusteam.caribeando.presenter.formatPhone
import com.agusteam.caribeando.presenter.profile.state.ProfileState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(
    private val getLocalUserProfile: GetLocalProfileUseCase,
    private val getProfileUseCase: GetUserProfileUseCase,
    private val saveLocalDataUseCase: SaveLocalDataUseCase,
    private val logoutUseCase: LogoutUseCase
) : GenericViewModel<ProfileState, ProfileEvent>(ProfileState()) {

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            if (Token.isInformationLoaded) {
                loadLocalProfile()
            } else {
                loadRemoteProfile()
            }

            setState { copy(isLoading = false) }
        }
    }

    private suspend fun loadLocalProfile() {
        getLocalUserProfile()
            .mapLatest { preferences ->
                val name = preferences[stringPreferencesKey(NAME)] ?: ""
                val lastName = preferences[stringPreferencesKey(LAST_NAME)] ?: ""
                val email = preferences[stringPreferencesKey(EMAIL)] ?: ""
                val phone = preferences[stringPreferencesKey(PHONE)] ?: ""

                setState {
                    copy(
                        name = name,
                        lastname = lastName,
                        email = email,
                        phone = formatPhone(phone)
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun loadRemoteProfile() {
        when (val result = getProfileUseCase()) {
            is OperationResult.Success -> {
                Token.isInformationLoaded = true
                saveLocalDataUseCase(
                    result.data.name,
                    result.data.lastname,
                    result.data.phone,
                    result.data.email
                )
                setState {
                    copy(
                        name = result.data.name,
                        lastname = result.data.lastname,
                        email = result.data.email,
                        phone = formatPhone(result.data.phone)
                    )
                }
            }

            is OperationResult.Error -> {
                onErrorHappened(
                    true,
                    title = "Error al cargar perfil",
                    message = result.exception.message ?: "Ocurrió un error inesperado."
                )
            }
        }
    }

    fun handleEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.LogoutUser -> logout()
            ProfileEvent.OnErrorModalAccepted -> clearError()
            else -> Unit
        }
    }

    private fun logout() {
        viewModelScope.launch {
            try {
                logoutUseCase()
                sendEvent(ProfileEvent.UserSessionClosed)
            } catch (e: Exception) {
                onErrorHappened(
                    true,
                    title = "Error inesperado",
                    message = "No se pudo completar la operación. Intente más tarde."
                )
            }
        }
    }

    private fun clearError() {
        viewModelScope.launch {
            onErrorHappened(false)
        }
    }

    private suspend fun onErrorHappened(value: Boolean, title: String = "", message: String = "") {
        val errorModel = if (value) ErrorModel(title, message) else null
        setState { copy(errorModel = errorModel) }
    }
}

sealed interface ProfileEvent {
    data object ProfileLoaded : ProfileEvent
    data object LogoutUser : ProfileEvent
    data object UserSessionClosed : ProfileEvent
    data object OnErrorModalAccepted : ProfileEvent
}
