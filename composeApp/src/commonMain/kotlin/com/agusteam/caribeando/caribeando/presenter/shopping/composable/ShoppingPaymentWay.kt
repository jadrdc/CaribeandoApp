package com.agusteam.caribeando.presenter.shopping.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.domain.models.PaymentModel
import com.agusteam.caribeando.presenter.common.CustomRadioButton
import com.agusteam.caribeando.presenter.formatMoney
import com.agusteam.caribeando.presenter.stripe.PaymentEvents
import com.agusteam.caribeando.presenter.stripe.PaymentState
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.booking_payment
import caribeando.composeapp.generated.resources.booking_payment_description
import caribeando.composeapp.generated.resources.payment_to_pay
import caribeando.composeapp.generated.resources.total_payment
import caribeando.composeapp.generated.resources.total_payment_description

/**
 * 2026-08-20 (design handoff pass): spec screen 14 puts "Pago de reserva solamente" before
 * "Pago total" -- the reverse of the previous order (total first). Cards now show their amount
 * via the restyled `CustomRadioButton` (previously the amount wasn't shown on the card at all,
 * only inside the description string).
 */
@Composable
fun ShoppingPaymentWay(
    state: PaymentState,
    onEvent: (PaymentEvents) -> Unit
) {
    val colors = caribeandoColors
    Column(verticalArrangement = Arrangement.spacedBy(11.dp)) {
        Text(
            text = stringResource(Res.string.payment_to_pay),
            color = colors.textPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
        CustomRadioButton(
            title = stringResource(Res.string.booking_payment),
            description = stringResource(
                Res.string.booking_payment_description,
                formatMoney(state.initialPayment.toInt())
            ),
            amount = formatMoney(state.initialPayment.toInt()),
            isSelected = PaymentModel.BOOKING_PAYMENT == state.selectedPaymentType
        ) {
            onEvent(
                PaymentEvents.OnPaymentTypePicked(
                    PaymentModel.BOOKING_PAYMENT
                )
            )
        }
        CustomRadioButton(
            title = stringResource(Res.string.total_payment),
            description = stringResource(
                Res.string.total_payment_description,
                formatMoney(state.totalPayment.toInt())
            ),
            amount = formatMoney(state.totalPayment.toInt()),
            isSelected = PaymentModel.TOTAL_PAYMENT == state.selectedPaymentType
        ) {
            onEvent(
                PaymentEvents.OnPaymentTypePicked(
                    PaymentModel.TOTAL_PAYMENT
                )
            )
        }
    }
}
