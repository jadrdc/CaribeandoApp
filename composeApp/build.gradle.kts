import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    // 2026-08-19: converted from `androidTarget {}` + a top-level `android {}` application block
    // to the AGP 9 `com.android.kotlin.multiplatform.library` plugin's `androidLibrary {}` DSL.
    // AGP 9 no longer allows the KMP plugin and com.android.application together in one module
    // (this module used to be both at once) — the actual Android application entry point
    // (MainActivity, the Application class, the manifest, launcher icons/resources,
    // google-services.json, the Firebase/Crashlytics/secrets Gradle plugins) now lives in the
    // sibling :androidApp module, which depends on this one.
    //
    // androidResources IS enabled here (unlike a first draft of this split): GoogleServiceProvider.kt
    // (androidMain) reads R.string.firebase_auth for the Google Sign-In web client ID. Because AGP's
    // android.nonTransitiveRClass=true means a module's generated R class only contains that module's
    // OWN resources — and :composeApp can never see :androidApp's resources, since the dependency
    // points the other way (:androidApp -> :composeApp) — this module needs its own small
    // src/androidMain/res/values/strings.xml carrying just the strings its own code references.
    // namespace deliberately DIFFERS from :androidApp's ("com.agusteam.caribeando"): a real
    // build failed with "Namespace ... is used in multiple modules" once this module started
    // shipping its own res/ with the same namespace as :androidApp. GoogleServiceProvider.kt's
    // R import was updated to com.agusteam.caribeando.shared.R to match.
    android {
        namespace = "com.agusteam.caribeando.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        androidResources {
            enable = true
        }

        @OptIn(ExperimentalKotlinGradlePluginApi::class) compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    // iosX64 (Intel simulator) intentionally dropped 2026-08-18: several KMP deps in this
    // project's current catalog (Coil3 3.5.0 / androidx.sqlite 2.7.0 era) no longer publish
    // iosX64 variants, since Apple deprecated Intel Macs. iosArm64 (real device) and
    // iosSimulatorArm64 (Apple Silicon simulator) still fully cover development and shipping.
    listOf(
        iosArm64(), iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            freeCompilerArgs += listOf("-Xdebug") // Enable debug symbols
        }
    }
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.android.maps.compose)
            implementation(libs.google.playServices.maps) // Google Maps SDK
            implementation(libs.stripe.android)
            implementation(libs.stripe.java)
            // Import the BoM for the Firebase platform
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.auth)
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.firestore)
            implementation(libs.play.services.auth)
            implementation(libs.androidx.core.i18n)

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.ktor.client.auth)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.sqlite.bundled)
            implementation(libs.android.lottie.compose) // Latest version as of now
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            api(libs.koin.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.encoding)
            implementation(libs.bundles.coil)
            implementation(libs.androidx.navigation.compose)
            api(libs.datastore.preferences)
            api(libs.datastore)
            implementation(libs.gitlive.firebase.crashlytics)
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            // Added 2026-08-18 alongside the cancellation/refund feature's first real unit test
            // (RequestOrderCancellationUseCaseTest) — commonTest previously had no test framework
            // wired in at all (ComposeAppCommonTest.kt was an empty stub with zero imports).
            implementation(kotlin("test"))
        }
    }
}
