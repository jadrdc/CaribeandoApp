package com.agusteam.caribeando.app

import android.app.Application
import com.agusteam.caribeando.R
import com.agusteam.caribeando.core.di.dataDiModule
import com.agusteam.caribeando.core.di.dataStorageDIModule
import com.agusteam.caribeando.core.di.diDomainModule
import com.agusteam.caribeando.core.di.networkModule
import com.agusteam.caribeando.core.di.viewModelModule
import com.google.firebase.FirebaseApp
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
        // 2026-08-19: guarded against a real launch crash -- "FirebaseApp name [DEFAULT] already
        // exists!". The com.google.gms.google-services plugin (+ google-services.json) auto-inits
        // Firebase via its own ContentProvider before Application.onCreate() ever runs, so this
        // explicit GitLive init call (needed on iOS, which has no such auto-init) was racing
        // against -- and losing to -- Android's automatic one. Skipping the explicit call when a
        // default app already exists fixes Android while leaving iOS behavior unchanged.
        if (FirebaseApp.getApps(applicationContext).isEmpty()) {
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
}
