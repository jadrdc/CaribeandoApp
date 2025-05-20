package com.agusteam.caribeando.domain.interfaces

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface LocalStoragePreferenceRepository {
    suspend fun save(key: String, value: String)
    suspend fun getValue(): Flow<Preferences>
    suspend fun clear()
}