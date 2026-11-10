package com.agusteam.caribeando.data.imp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.agusteam.caribeando.data.database.SecureStorage
import com.agusteam.caribeando.data.database.createDataStore
import com.agusteam.caribeando.data.util.REFRESH_TOKEN
import com.agusteam.caribeando.data.util.TOKEN
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okio.FileSystem
import okio.Path.Companion.toPath
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

private class FakeSecureStorage : SecureStorage {
    val values = mutableMapOf<String, String>()
    override fun get(key: String) = values[key]
    override fun put(key: String, value: String) { values[key] = value }
    override fun remove(key: String) { values.remove(key) }
    override fun clear() = values.clear()
}

class LocalStorageDataStoreTest {

    private fun newDataStore(): DataStore<Preferences> {
        val dir = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
        val file = dir / "caribeando-test-${Random.nextLong()}.preferences_pb"
        return createDataStore { file.toString() }
    }

    private val tokenKey = stringPreferencesKey(TOKEN)
    private val refreshKey = stringPreferencesKey(REFRESH_TOKEN)
    private val otherKey = stringPreferencesKey("OTHER")

    @Test
    fun tokensGoToSecureStorageNotThePlainDataStore() = runBlocking {
        val plain = newDataStore()
        val secure = FakeSecureStorage()
        val store = LocalStorageDataStore(plain, secure)

        store.save(TOKEN, "access")
        store.save(REFRESH_TOKEN, "refresh")
        store.save("OTHER", "visible")

        assertEquals("access", secure.get(TOKEN))
        assertEquals("refresh", secure.get(REFRESH_TOKEN))
        val raw = plain.data.first()
        assertNull(raw[tokenKey])
        assertNull(raw[refreshKey])
        assertEquals("visible", raw[otherKey])
    }

    @Test
    fun readingMergesSecureValuesBackIn() = runBlocking {
        val store = LocalStorageDataStore(newDataStore(), FakeSecureStorage())
        store.save(TOKEN, "access")
        store.save("OTHER", "visible")

        val prefs = store.getValue().first()

        assertEquals("access", prefs[tokenKey])
        assertEquals("visible", prefs[otherKey])
    }

    @Test
    fun legacyPlaintextTokensAreMigratedAndRemoved() = runBlocking {
        val plain = newDataStore()
        plain.edit {
            it[tokenKey] = "old-access"
            it[refreshKey] = "old-refresh"
        }
        val secure = FakeSecureStorage()
        val store = LocalStorageDataStore(plain, secure)

        val prefs = store.getValue().first()

        assertEquals("old-access", prefs[tokenKey])
        assertEquals("old-access", secure.get(TOKEN))
        assertEquals("old-refresh", secure.get(REFRESH_TOKEN))
        assertNull(plain.data.first()[tokenKey])
        assertNull(plain.data.first()[refreshKey])
    }

    @Test
    fun clearWipesBothStores() = runBlocking {
        val plain = newDataStore()
        val secure = FakeSecureStorage()
        val store = LocalStorageDataStore(plain, secure)
        store.save(TOKEN, "access")
        store.save("OTHER", "visible")

        store.clear()

        assertNull(secure.get(TOKEN))
        assertNull(plain.data.first()[otherKey])
        assertNull(store.getValue().first()[tokenKey])
    }
}
