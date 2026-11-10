package com.agusteam.caribeando.presenter.shopping.screen

import org.jetbrains.compose.resources.stringResource
import caribeando.composeapp.generated.resources.amount_paid
import caribeando.composeapp.generated.resources.keep_exploring
import caribeando.composeapp.generated.resources.see_my_trips
import caribeando.composeapp.generated.resources.success_message
import caribeando.composeapp.generated.resources.success_title
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.agusteam.caribeando.presenter.common.ActionButton
import com.agusteam.caribeando.presenter.common.TextLink
import com.agusteam.caribeando.presenter.common.dashedBorder
import com.agusteam.caribeando.presenter.formatMoney
import com.agusteam.caribeando.presenter.home.navigation.HomeTabRequest
import com.agusteam.caribeando.presenter.home.navigation.NavigationRoutes
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.painterResource
import caribeando.composeapp.generated.resources.Res
import caribeando.composeapp.generated.resources.logo

/**
 * The handoff's "16. Listo" [replaces the old post-payment screen]. Centered: 104x104 cream tile
 * radius 30 with the logo at 80x80 radius 22, entering with a pop. 26dp: "Listo" 600/26. 8dp:
 * "Tu lugar en <viaje> esta reservado. Te esperamos el <fecha>." 26dp: a dashed confirmation-code
 * block. 20dp: "Monto pagado" against the amount. Bottom: PrimaryButton "Ver mis viajes", then
 * "Seguir explorando".
 *
 * 2026-08-20 (design handoff pass): new screen, inserted between a successful Stripe payment and
 * where the app used to land directly on the order-detail screen (`ShoppingOrderDetailScreen`,
 * which is really task-26's "19. Detalles de la reserva", not a success confirmation).
 *
 * 2026-10-09: RESOLVED -- "Ver mis viajes" now records a one-shot tab request
 * (`HomeTabRequest`) that HomeScreen honors on arrival, so it lands on Viajes. Original note:
 * Known limitation, flagged rather than faked: "Ver mis viajes" is specced to land on the Viajes
 * tab specifically. This screen sits deep inside a nested shopping NavHost with no existing hook
 * to deep-link into a specific HomeScreen bottom-nav tab (that would need new state threaded
 * through MainNavigationFlow -> HomeScreen, a bigger nav refactor than this pass should risk
 * untested). Both [onViewTrips] and [onKeepExploring] are wired to the same "back to Home" exit
 * this flow already uses elsewhere -- functionally safe, but "Ver mis viajes" doesn't actually
 * preselect the Viajes tab yet.
 */
@Composable
fun PaymentSuccessScreen(
    tripTitle: String,
    leavingDate: String,
    confirmationCode: String,
    amountPaid: Double,
    onViewTrips: () -> Unit,
    onKeepExploring: () -> Unit,
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
                .size(104.dp)
                .scale(popScale.value)
                .background(colors.cream, RoundedCornerShape(30.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(22.dp))
            )
        }
        Text(
            modifier = Modifier.padding(top = 26.dp),
            text = stringResource(Res.string.success_title),
            color = colors.textPrimary,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(Res.string.success_message, tripTitle, leavingDate),
            color = colors.textSecondary,
            fontSize = 13.sp,
            lineHeight = 19.5.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal
        )
        Column(
            modifier = Modifier
                .padding(top = 26.dp)
                .fillMaxWidth()
                .dashedBorder(colors.dashedBorder, cornerRadius = 16.dp)
                .padding(horizontal = 16.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CÓDIGO DE CONFIRMACIÓN",
                color = colors.textSecondary,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                modifier = Modifier.padding(top = 6.dp),
                text = confirmationCode,
                color = colors.textPrimary,
                fontSize = 20.sp,
                letterSpacing = 0.06.em,
                fontWeight = FontWeight.SemiBold
            )
        }
        Row(
            modifier = Modifier.padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.amount_paid),
                color = colors.textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = formatMoney(amountPaid.toInt()),
                color = colors.redFill,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Column(
            modifier = Modifier.padding(top = 40.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionButton(
                text = stringResource(Res.string.see_my_trips),
                modifier = Modifier.fillMaxWidth(),
                // 2026-10-09: lands on the Viajes tab (see HomeTabRequest), not just Home.
                onClick = {
                    HomeTabRequest.request(NavigationRoutes.OrderHistoryScreen)
                    onViewTrips()
                }
            )
            Box(Modifier.padding(top = 12.dp)) {
                TextLink(text = stringResource(Res.string.keep_exploring), onClick = onKeepExploring)
            }
        }
    }
}
