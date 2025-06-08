package com.agusteam.caribeando

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.agusteam.caribeando.presenter.signup.navigation.MainNavigationFlow
import com.agusteam.caribeando.presenter.theme.backGround

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = backGround.toArgb() // your background color

        // Optional: control icon color (dark or light)
        WindowCompat.setDecorFitsSystemWindows(window, true) // optional if you're handling insets manually
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true // true = dark icons

        setContent {
            Box(
                Modifier
                    .background(backGround).fillMaxSize()
            ) {
                MainNavigationFlow()
            }
        }
    }
}