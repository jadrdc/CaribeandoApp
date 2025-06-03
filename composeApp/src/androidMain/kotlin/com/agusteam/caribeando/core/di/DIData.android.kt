package com.agusteam.caribeando.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.agusteam.caribeando.core.auth.GoogleAuthUiClient
import com.agusteam.caribeando.data.database.createDataStore
import com.agusteam.caribeando.data.network.createHttpClient
import com.agusteam.caribeando.domain.usecase.PlatformContext
import com.agusteam.caribeando.presenter.social.SocialSignViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

actual val networkModule: Module
    get() = module {
        single<HttpClient> { createHttpClient(OkHttp.create()) }
    }
actual val dataStorageDIModule: Module
    get() = module {
        single<SignInClient> { Identity.getSignInClient(androidContext()) }
        single<GoogleAuthUiClient> { GoogleAuthUiClient(androidContext(), get()) }
        viewModel<SocialSignViewModel> { SocialSignViewModel(get(),get(), get()) }
        single { PlatformContext(androidContext()) }
        single<DataStore<Preferences>> {
            createDataStore(androidContext())
        }
    }