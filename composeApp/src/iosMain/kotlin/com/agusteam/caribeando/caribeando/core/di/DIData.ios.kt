package com.agusteam.caribeando.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.agusteam.caribeando.caribeando.core.StripeNativeBridge
import com.agusteam.caribeando.caribeando.data.util.CrashReporter
import com.agusteam.caribeando.data.database.createDataStore
import com.agusteam.caribeando.data.network.createHttpClient
import com.agusteam.caribeando.domain.usecase.PlatformContext
import com.agusteam.caribeando.domain.usecase.StartStripeUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual val networkModule: Module
    get() = module {
        single<HttpClient> { createHttpClient(Darwin.create()) }
    }
actual val dataStorageDIModule: Module
    get() = module {
        single<DataStore<Preferences>> {
            createDataStore()
        }
        single { PlatformContext() }
        single<StartStripeUseCase> { StartStripeUseCase(get()) }
        single<CrashReporter> {
            CrashReporter()
        }

    }
