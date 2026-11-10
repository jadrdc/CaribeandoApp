package com.agusteam.caribeando.caribeando.presenter.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp

/**
 * "Message on WhatsApp" call-to-action button.
 *
 * Builds a wa.me deep link from a raw phone number (as stored on
 * BusinessModel.phone / BusinessModelSummary.phone) and an optional prefilled message, then
 * opens it through Compose's LocalUriHandler — a stable, built-in composition local that
 * already works on Android, iOS, and Desktop. No expect/actual platform code and no new
 * Gradle dependency needed.
 *
 * Suggested call sites:
 *  - TripProviderProfileScreen, next to the provider's name/stats.
 *  - Trip detail screen (ShoppingItemDetailScreen), near the meeting point / cancellation
 *    policy, so a traveler can ask a question before booking.
 *
 * Example:
 *   WhatsAppButton(
 *       phone = business.phone,
 *       prefilledMessage = "Hi! I have a question about \"${trip.name}\".",
 *   )
 */
@Composable
fun WhatsAppButton(
    phone: String,
    prefilledMessage: String? = null,
    modifier: Modifier = Modifier,
    label: String = "Message on WhatsApp",
) {
    val uriHandler = LocalUriHandler.current
    val digitsOnly = phone.filter { it.isDigit() }

    // No usable phone number on file — hide the CTA instead of opening a dead wa.me link.
    if (digitsOnly.isBlank()) return

    Surface(
        onClick = { uriHandler.openUri(buildWhatsAppUrl(digitsOnly, prefilledMessage)) },
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = WhatsAppGreen,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Speech-bubble glyph instead of a Material icon, so this doesn't require adding
            // the material-icons-extended dependency to the project.
            Text(text = "💬", style = MaterialTheme.typography.labelLarge)
            Text(text = label, color = Color.White, style = MaterialTheme.typography.labelLarge)
        }
    }
}

private val WhatsAppGreen = Color(0xFF25D366)

internal fun buildWhatsAppUrl(phoneDigitsOnly: String, message: String?): String {
    val base = "https://wa.me/$phoneDigitsOnly"
    if (message.isNullOrBlank()) return base
    return "$base?text=${message.encodeUrlParam()}"
}

// Minimal, dependency-free query-param encoder. Deliberately avoids java.net.URLEncoder
// (JVM/Android-only) so this file compiles unmodified for the iOS and Desktop targets too.
private fun String.encodeUrlParam(): String = buildString {
    for (ch in this@encodeUrlParam) {
        when {
            ch.isLetterOrDigit() || ch in "-_.~" -> append(ch)
            ch == ' ' -> append("%20")
            ch.code < 128 -> {
                append('%')
                append(ch.code.toHexByte())
            }
            else -> append(ch) // non-ASCII left as-is; wa.me accepts raw UTF-8 in practice
        }
    }
}

private fun Int.toHexByte(): String {
    val digits = "0123456789ABCDEF"
    return "${digits[(this shr 4) and 0xF]}${digits[this and 0xF]}"
}
