package com.agusteam.caribeando.presenter.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class QrCodeMatrixTest {

    @Test
    fun matrixIsSquareAndHasDarkModules() {
        val matrix = qrCodeMatrix("pi_3Nabc123")
        assertTrue(matrix.isNotEmpty())
        matrix.forEach { row -> assertEquals(matrix.size, row.size) }
        assertTrue(matrix.any { row -> row.any { it } })
    }

    @Test
    fun differentContentGivesDifferentMatrix() {
        assertNotEquals(qrCodeMatrix("ORDER-0001"), qrCodeMatrix("ORDER-0002"))
    }

    @Test
    fun sameContentIsDeterministic() {
        assertEquals(qrCodeMatrix("ORDER-0001"), qrCodeMatrix("ORDER-0001"))
    }
}
