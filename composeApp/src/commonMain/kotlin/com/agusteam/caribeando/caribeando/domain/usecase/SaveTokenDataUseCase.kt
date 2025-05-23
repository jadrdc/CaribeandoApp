package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.data.model.Token
import com.agusteam.caribeando.data.util.REFRESH_TOKEN
import com.agusteam.caribeando.data.util.TOKEN
import com.agusteam.caribeando.domain.interfaces.LocalStoragePreferenceRepository
import com.agusteam.caribeando.domain.models.TokenMode

class SaveTokenDataUseCase(private val localStorageDataStore: LocalStoragePreferenceRepository) {
    suspend operator fun invoke(model: TokenMode) {
        Token.token = model.accessToken
        Token.refreshToken = model.refreshToken

        localStorageDataStore.save(REFRESH_TOKEN, model.refreshToken)
        localStorageDataStore.save(TOKEN, model.accessToken)
    }
}