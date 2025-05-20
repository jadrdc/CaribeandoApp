package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.data.model.Token
import com.agusteam.caribeando.domain.interfaces.LocalStoragePreferenceRepository

class LogoutUseCase(private val repository: LocalStoragePreferenceRepository) {
    suspend operator fun invoke() {
        Token.logout()
        return repository.clear()
    }
}