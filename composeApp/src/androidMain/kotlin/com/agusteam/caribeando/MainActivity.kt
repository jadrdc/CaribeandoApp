package com.agusteam.caribeando

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.agusteam.caribeando.presenter.signup.navigation.MainNavigationFlow
import com.agusteam.caribeando.presenter.theme.backGround

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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