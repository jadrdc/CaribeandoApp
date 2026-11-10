package com.agusteam.caribeando.data.imp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.agusteam.caribeando.data.database.SecureStorage
import com.agusteam.caribeando.data.util.REFRESH_TOKEN
import com.agusteam.caribeando.data.util.TOKEN
import com.agusteam.caribeando.domain.interfaces.LocalStoragePreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Preferences storage. Values under [SECRET_KEYS] (the auth tokens) are kept in [secureStorage]
 * instead of the plain DataStore, but callers still save and read them through the same API, so
 * nothing else in the app had to change.
 */
class LocalStorageDataStore(
    private val storage: DataStore<Preferences>,
    private val secureStorage: SecureStorage
) : LocalStoragePreferenceRepository {

    override suspend fun save(key: String, value: String) {
        if (key in SECRET_KEYS) {
            secureStorage.put(key, value)
            // Never leave a plaintext copy behind (e.g. one written by an older app version).
            storage.edit { it.remove(stringPreferencesKey(key)) }
            return
        }
        storage.edit { dataStore ->
            val valueKey = stringPreferencesKey(key)
            dataStore[valueKey] = value
        }
    }

    override suspend fun save(key: String, value: Boolean) {
        storage.edit { dataStore ->
            val valueKey = booleanPreferencesKey(key)
            dataStore[valueKey] = value
        }
    }

    override suspend fun getValue(): Flow<Preferences> {
        migrateLegacyTokens()
        return storage.data.map { preferences ->
            val withSecrets = preferences.toMutablePreferences()
            SECRET_KEYS.forEach { key ->
                secureStorage.get(key)?.let { withSecrets[stringPreferencesKey(key)] = it }
            }
            withSecrets
        }
    }

    override suspend fun clear() {
        secureStorage.clear()
        storage.edit { preference ->
            preference.clear()
        }
    }

    /**
     * Tokens saved by earlier versions are plaintext in the DataStore. Move them into secure
     * storage once and delete the plaintext, so existing sessions survive the upgrade.
     */
    private suspend fun migrateLegacyTokens() {
        storage.edit { preferences ->
            SECRET_KEYS.forEach { key ->
                val prefKey = stringPreferencesKey(key)
                val legacy = preferences[prefKey] ?: return@forEach
                if (secureStorage.get(key) == null) secureStorage.put(key, legacy)
                preferences.remove(prefKey)
            }
        }
    }

    companion object {
        val SECRET_KEYS = setOf(TOKEN, REFRESH_TOKEN)
    }
}
