package com.agusteam.caribeando.data.imp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.agusteam.caribeando.data.model.UpcomingOrderTripModelResponse
import kotlinx.coroutines.flow.first
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

/** JSON (de)serialization of the cached order list. Kept separate so it can be unit tested. */
object OrderCacheCodec {
    private val json = Json { ignoreUnknownKeys = true }
    private val serializer = ListSerializer(UpcomingOrderTripModelResponse.serializer())

    fun encode(orders: List<UpcomingOrderTripModelResponse>): String =
        json.encodeToString(serializer, orders)

    /** Returns null when the text is not a valid cached list (e.g. the format changed). */
    fun decode(text: String): List<UpcomingOrderTripModelResponse>? =
        runCatching { json.decodeFromString(serializer, text) }.getOrNull()
}

/**
 * Last successfully loaded upcoming orders, so tickets stay available without a connection.
 * It lives in the app's preferences DataStore, which logout clears, so one user's orders are never
 * shown to the next.
 */
class OrderCache(private val storage: DataStore<Preferences>) {
    private val key = stringPreferencesKey("cached_upcoming_orders")

    suspend fun save(orders: List<UpcomingOrderTripModelResponse>) {
        storage.edit { it[key] = OrderCacheCodec.encode(orders) }
    }

    suspend fun load(): List<UpcomingOrderTripModelResponse>? =
        storage.data.first()[key]?.let(OrderCacheCodec::decode)
}
