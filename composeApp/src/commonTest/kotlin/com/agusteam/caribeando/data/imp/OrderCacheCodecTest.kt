package com.agusteam.caribeando.data.imp

import com.agusteam.caribeando.data.model.UpcomingOrderTripBusinessModel
import com.agusteam.caribeando.data.model.UpcomingOrderTripModelResponse
import com.agusteam.caribeando.data.model.UpcomingOrderTripScheduleModel
import com.agusteam.caribeando.data.model.UpcomingTripBusinessProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Instant

class OrderCacheCodecTest {

    private val order = UpcomingOrderTripModelResponse(
        id = "txn_1",
        orderId = "order_1",
        scheduledId = "sched_1",
        evaluated = false,
        tripScheduleModel = UpcomingOrderTripScheduleModel(
            leaving_time = Instant.fromEpochSeconds(1_800_000_000),
            total_payment = 150.0,
            paid_amount = 50.0,
            returning_time = Instant.fromEpochSeconds(1_800_040_000),
            tripModel = UpcomingOrderTripBusinessModel(
                businessModel = UpcomingTripBusinessProvider(image = "img", name = "Operator", month = 3),
                destiny = "Punta Cana",
                images = listOf("a.jpg", "b.jpg"),
                name = "Saona Island"
            )
        )
    )

    @Test
    fun roundTripKeepsEveryField() {
        val decoded = OrderCacheCodec.decode(OrderCacheCodec.encode(listOf(order)))
        assertEquals(listOf(order), decoded)
    }

    @Test
    fun emptyListRoundTrips() {
        assertEquals(emptyList(), OrderCacheCodec.decode(OrderCacheCodec.encode(emptyList())))
    }

    @Test
    fun corruptCacheIsTreatedAsMissing() {
        assertNull(OrderCacheCodec.decode("not json"))
        assertNull(OrderCacheCodec.decode("{\"unexpected\": true}"))
    }
}
