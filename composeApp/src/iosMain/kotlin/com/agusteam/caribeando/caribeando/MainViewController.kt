package com.agusteam.caribeando

import androidx.compose.ui.window.ComposeUIViewController
import com.agusteam.caribeando.core.di.dataDiModule
import com.agusteam.caribeando.core.di.dataStorageDIModule
import com.agusteam.caribeando.core.di.diDomainModule
import com.agusteam.caribeando.core.di.networkModule
import com.agusteam.caribeando.core.di.viewModelModule
import com.agusteam.caribeando.presenter.signup.navigation.MainNavigationFlow
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(configure = {
    startKoin {
        modules(
            diDomainModule, viewModelModule, dataDiModule, networkModule,
            dataStorageDIModule
        )
    }
}) {
    MainNavigationFlow()
}