package com.agusteam.caribeando.app

import android.app.Application
import com.agusteam.caribeando.R
import com.agusteam.caribeando.core.di.dataDiModule
import com.agusteam.caribeando.core.di.dataStorageDIModule
import com.agusteam.caribeando.core.di.diDomainModule
import com.agusteam.caribeando.core.di.networkModule
import com.agusteam.caribeando.core.di.viewModelModule
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CaribeandoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CaribeandoApp)
            modules(
                viewModelModule,
                diDomainModule,
                networkModule,
                dataDiModule,
                dataStorageDIModule
            )
        }
        Firebase.initialize(
            applicationContext,
            options = FirebaseOptions(
                applicationId = this.getString(R.string.firebase_app_id),
                apiKey = this.getString(R.string.firebase_api_key),
                projectId = this.getString(R.string.firebase_project_id)
            )
        )
    }
}