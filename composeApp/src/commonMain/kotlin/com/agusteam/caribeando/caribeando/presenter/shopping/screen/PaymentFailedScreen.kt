package com.agusteam.caribeando.presenter.shopping.screen

import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.back_to_explore
import caribeando.composeapp.generated.resources.change_payment_method
import caribeando.composeapp.generated.resources.label_amount
import caribeando.composeapp.generated.resources.label_trip
import caribeando.composeapp.generated.resources.payment_failed_default_reason
import caribeando.composeapp.generated.resources.payment_failed_not_charged
import caribeando.composeapp.generated.resources.payment_failed_title
import caribeando.composeapp.generated.resources.try_again
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.ListGroup
import com.agusteam.caribeando.presenter.common.ListGroupLabelValueRow
import com.agusteam.caribeando.presenter.common.SecondaryButton
import com.agusteam.caribeando.presenter.common.TextLink
import com.agusteam.caribeando.presenter.formatMoney
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.painterResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.cancel

/**
 * The handoff's "17. Pago fallido" [NEW] -- backend already exists (`payment/order-failure`,
 * `CancelPaymentOrderUseCase`, already called by `PaymentViewModel` on every failure). Centered:
 * 96x96 tile radius 28 with the "cancel" icon at 46, popping in. 26dp: headline 600/24/1.25.
 * 10dp: the failure reason. 24dp: a ListGroup (Viaje / Monto / Método). 16dp: "No se te cobró
 * nada..." Bottom: PrimaryButton "Intentar de nuevo", SecondaryButton "Cambiar método de pago",
 * muted link "Volver a explorar".
 *
 * 2026-08-20 (design handoff pass): new screen, reached when Stripe reports a failed/cancelled
 * payment sheet (`PaymentViewModel.PaymentEvents.OnPaymentFailed`, wired via the events channel
 * in `TripItemPayingScreen`) -- this used to just show a generic ErrorModal with no way to
 * recover except backing all the way out. The spec's "Método" row (last 4 digits / card brand)
 * is left out of the ListGroup rather than faked -- same gap noted on screen 14, there's no real
 * saved-payment-method data anywhere in this app.
 */
@Composable
fun PaymentFailedScreen(
    tripTitle: String,
    amount: Double,
    reason: String,
    onRetry: () -> Unit,
    onChangePaymentMethod: () -> Unit,
    onBackToExplore: () -> Unit,
) {
    val colors = caribeandoColors
    val popScale = remember { Animatable(0.7f) }
    LaunchedEffect(Unit) {
        popScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .scale(popScale.value)
                .background(colors.redTint, RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.cancel),
                contentDescription = null,
                tint = colors.redFill,
                modifier = Modifier.size(46.dp)
            )
        }
        Text(
            modifier = Modifier.padding(top = 26.dp),
            text = stringResource(Res.string.payment_failed_title),
            color = colors.textPrimary,
            fontSize = 24.sp,
            lineHeight = 30.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = reason.ifBlank { stringResource(Res.string.payment_failed_default_reason) },
            color = colors.textSecondary,
            fontSize = 13.sp,
            lineHeight = 19.5.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal
        )
        Box(Modifier.padding(top = 24.dp).fillMaxWidth()) {
            ListGroup(
                rows = listOf(
                    { ListGroupLabelValueRow(label = stringResource(Res.string.label_trip), value = tripTitle) },
                    { ListGroupLabelValueRow(label = stringResource(Res.string.label_amount), value = formatMoney(amount.toInt())) },
                )
            )
        }
        Text(
            modifier = Modifier.padding(top = 16.dp),
            // 2026-10-09: dropped the spec's "Guardamos tu lugar por 30 minutos." -- nothing holds
            // the seat: PaymentViewModel cancels the pending order as soon as the payment fails,
            // so that promise wasn't true. Re-add it only if a real hold is implemented.
            text = stringResource(Res.string.payment_failed_not_charged),
            color = colors.textSecondary,
            fontSize = 11.5.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal
        )
        Column(
            modifier = Modifier.padding(top = 32.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionButton(
                text = stringResource(Res.string.try_again),
                modifier = Modifier.fillMaxWidth(),
                onClick = onRetry
            )
            Box(Modifier.padding(top = 12.dp).fillMaxWidth()) {
                SecondaryButton(
                    text = stringResource(Res.string.change_payment_method),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onChangePaymentMethod
                )
            }
            Box(Modifier.padding(top = 16.dp)) {
                TextLink(text = stringResource(Res.string.back_to_explore), onClick = onBackToExplore, muted = true)
            }
        }
    }
}
