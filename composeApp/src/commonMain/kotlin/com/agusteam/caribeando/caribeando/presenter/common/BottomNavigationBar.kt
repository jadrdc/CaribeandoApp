package com.agusteam.caribeando.presenter.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.agusteam.caribeando.presenter.home.model.BottomNavigationItem
import com.agusteam.caribeando.presenter.theme.caribeandoColors
import org.jetbrains.compose.resources.painterResource

/**
 * The handoff's "BottomNav" shared component: "Four items -- Explorar, Favoritos, Viajes,
 * Perfil -- evenly spaced, 1dp #EEF0F1 top border, 12dp top / 26dp bottom padding. Icon
 * 22-23 above a 10pt label, 5dp gap. Active #D62828 with a 500 label; inactive #8A9298 with
 * a 400 label."
 *
 * 2026-08-20 (screens 18-30 fidelity pass): this bar was still on M3's stock `NavigationBar`/
 * `NavigationBarItem` with the pre-redesign `grey500`/`primary` colors (not `caribeandoColors`,
 * so it wouldn't follow dark mode), a 24dp icon (spec wants 22-23), no top border at all, and
 * no font-weight distinction between the active/inactive label (only a color change). Rebuilt
 * as a plain Row of equally-weighted columns so every one of those details -- icon size, the
 * label's weight, the exact border/padding -- can be set directly instead of fighting M3's
 * default slot styling.
 */
@Composable
fun BottomNavigationBar(
    navController: NavController, visible: Boolean
) {
    val colors = caribeandoColors
    val options = listOf(
        BottomNavigationItem.Explore,
        BottomNavigationItem.WishList,
        BottomNavigationItem.OrderHistoric,
        BottomNavigationItem.Profile
    )
    val density = LocalDensity.current
    val currentRoute =
        navController.currentBackStackEntryFlow.collectAsState(initial = null).value?.destination?.route

    AnimatedVisibility(visible = visible, enter = slideInVertically {
        with(density) { 40.dp.roundToPx() }
    } + fadeIn(
        initialAlpha = 0.3f
    ), exit = slideOutVertically(targetOffsetY = {
        with(density) { 40.dp.roundToPx() }
    }) + fadeOut()) {
        Column(Modifier.fillMaxWidth().background(colors.pageBackground)) {
            Row(Modifier.fillMaxWidth().height(1.dp).background(colors.hairline)) {}
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 26.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                options.forEach { item ->
                    val isSelected = currentRoute == item.route
                    val tint = if (isSelected) colors.redFill else colors.iconInactive
                    Column(
                        modifier = Modifier.bounceClick(scaleDown = 0.9f) {
                            navController.navigate(item.route)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            tint = tint,
                            modifier = Modifier.size(22.dp),
                            contentDescription = null,
                            painter = painterResource(item.drawableResource),
                        )
                        Text(
                            modifier = Modifier.padding(top = 5.dp),
                            text = item.title,
                            color = tint,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                        )
                    }
                }
            }
        }
    }
}
