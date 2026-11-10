plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.agusteam.caribeando"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.agusteam.caribeando"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // The actual app entry point (MainActivity, CaribeandoApp Application class, manifest,
    // launcher icons) — everything else (screens, viewmodels, platform `actual`
    // implementations) lives in :composeApp as a KMP library, per the AGP 9 module split
    // (AGP 9+ no longer allows the KMP plugin and com.android.application in the same module).
    implementation(project(":composeApp"))

    implementation(compose.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    // CaribeandoApp.kt calls dev.gitlive.firebase.Firebase.initialize(...) directly — needs the
    // GitLive Firebase symbols on THIS module's own compile classpath (an `implementation` dep
    // of :composeApp doesn't leak onto a downstream module's compile classpath in Gradle).
    implementation(project.dependencies.platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore)
    implementation(libs.gitlive.firebase.crashlytics)
    implementation(libs.androidx.core.i18n)
    debugImplementation(compose.uiTooling)
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.defaults.properties"
}
