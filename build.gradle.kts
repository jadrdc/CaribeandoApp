plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    // 2026-08-19: swapped the old com.android.library alias for the AGP 9
    // com.android.kotlin.multiplatform.library plugin — :composeApp is now a KMP library
    // module using the new androidLibrary {} DSL, not a plain com.android.library module.
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint) apply false
    id("com.google.gms.google-services") version "4.5.0" apply false
    id("com.google.firebase.crashlytics") version "3.0.7" apply false
}
buildscript {
    dependencies {
        classpath(libs.maps.secrets.gradle.plugin)
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    extensions.configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        // Existing code predates the linter: the baseline file records current violations so CI only
        // fails on new ones. Regenerate with `./gradlew ktlintGenerateBaseline` after a cleanup pass.
        baseline.set(file("config/ktlint-baseline.xml"))
        filter { exclude { it.file.path.contains("/build/") } }
    }
}
