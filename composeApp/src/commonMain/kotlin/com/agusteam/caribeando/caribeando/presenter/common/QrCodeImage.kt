package com.agusteam.caribeando.presenter.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import qrcode.QRCode
import qrcode.raw.ErrorCorrectionLevel

/** Dark/light modules of a QR code for [content]; `true` is a dark module. */
fun qrCodeMatrix(content: String): List<List<Boolean>> =
    QRCode.ofSquares()
        .withErrorCorrectionLevel(ErrorCorrectionLevel.MEDIUM)
        .build(content)
        .rawData
        .map { row -> row.map { square -> square.dark } }

/**
 * Draws a QR code for [content]. It is always dark-on-white with a quiet zone, whatever the app
 * theme, because scanners need that contrast.
 */
@Composable
fun QrCodeImage(
    content: String,
    modifier: Modifier = Modifier,
) {
    val matrix = remember(content) { qrCodeMatrix(content) }
    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .background(Color.White)
            .padding(12.dp)
    ) {
        val modules = matrix.size
        if (modules == 0) return@Canvas
        val cell = size.width / modules
        matrix.forEachIndexed { row, cols ->
            cols.forEachIndexed { col, dark ->
                if (dark) {
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(col * cell, row * cell),
                        size = Size(cell + 0.5f, cell + 0.5f)
                    )
                }
            }
        }
    }
}
