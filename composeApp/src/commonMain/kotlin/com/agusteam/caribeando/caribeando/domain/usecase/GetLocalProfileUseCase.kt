package com.agusteam.caribeando.domain.usecase

import androidx.datastore.preferences.core.Preferences
import com.agusteam.caribeando.domain.interfaces.LocalStoragePreferenceRepository
import kotlinx.coroutines.flow.Flow

class GetLocalProfileUseCase(private val repository: LocalStoragePreferenceRepository) {

    suspend operator fun invoke(): Flow<Preferences> {
        return repository.getValue()
    }
}