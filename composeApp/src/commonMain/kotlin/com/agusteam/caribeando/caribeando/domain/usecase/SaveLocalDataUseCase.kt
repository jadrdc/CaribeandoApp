package com.agusteam.caribeando.domain.usecase

import com.agusteam.caribeando.data.util.EMAIL
import com.agusteam.caribeando.data.util.LAST_NAME
import com.agusteam.caribeando.data.util.NAME
import com.agusteam.caribeando.data.util.PHONE
import com.agusteam.caribeando.domain.interfaces.LocalStoragePreferenceRepository

class SaveLocalDataUseCase(private val localStorageDataStore: LocalStoragePreferenceRepository) {
    suspend operator fun invoke(name: String, lastName: String, phone: String, email: String) {
        localStorageDataStore.save(NAME, name)
        localStorageDataStore.save(LAST_NAME, lastName)
        localStorageDataStore.save(PHONE, phone)
        localStorageDataStore.save(EMAIL, email)
    }
}