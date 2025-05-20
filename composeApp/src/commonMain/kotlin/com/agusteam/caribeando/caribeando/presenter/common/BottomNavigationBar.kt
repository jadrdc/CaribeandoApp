package com.agusteam.caribeando.presenter.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.agusteam.caribeando.presenter.home.model.BottomNavigationItem
import com.agusteam.caribeando.presenter.theme.backGround
import com.agusteam.caribeando.presenter.theme.primary
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomNavigationBar(
    navController: NavController, visible: Boolean
) {
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
        NavigationBar(
            modifier = Modifier,
            containerColor = backGround,
        ) {
            options.forEach { item ->
                val isSelected = currentRoute == item.route

                NavigationBarItem(selected = false,
                    icon = {
                        Icon(
                            tint = if (isSelected) primary else Color.Gray,
                            modifier = Modifier.size(24.dp),
                            contentDescription = null,
                            painter = painterResource(item.drawableResource),
                        )
                    }, label = {
                        Text(
                            text = item.title, color = if (isSelected) primary else Color.Gray
                        )
                    }, onClick = {
                        navController.navigate(item.route)
                    })
            }
        }
    }
}
