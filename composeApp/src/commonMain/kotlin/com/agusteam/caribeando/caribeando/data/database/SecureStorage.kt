package com.agusteam.caribeando.data.database

import com.liftric.kvault.KVault

/**
 * Storage for secrets such as auth tokens. Backed by the Android Keystore (encrypted
 * preferences) and the iOS Keychain, never by the plain preferences DataStore.
 */
interface SecureStorage {
    fun get(key: String): String?
    fun put(key: String, value: String)
    fun remove(key: String)
    fun clear()
}

class KVaultSecureStorage(private val vault: KVault) : SecureStorage {
    override fun get(key: String): String? = vault.string(key)
    override fun put(key: String, value: String) {
        vault.set(key, value)
    }

    override fun remove(key: String) {
        vault.deleteObject(key)
    }

    override fun clear() {
        vault.clear()
    }
}
