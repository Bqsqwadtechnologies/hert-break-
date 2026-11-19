import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val companion = Properties().apply {
    rootProject.file("config/companion.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}
fun cfg(key: String, fallback: String) = companion.getProperty(key)?.trim().takeUnless { it.isNullOrEmpty() } ?: fallback

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}
android {
    namespace = "com.bqsqwad.heartbreakhealer.feature.heartjourney"
    compileSdk = 36
    defaultConfig {
        minSdk = 26
        buildConfigField("String", "COMPANION_WEB_URL", "\"${cfg("WEB_URL", "https://bqsqwadai.com")}\"")
        buildConfigField("String", "COMPANION_STORE_URL", "\"${cfg("STORE_URL", "")}\"")
        buildConfigField("String", "COMPANION_PACKAGE", "\"${cfg("PACKAGE", "")}\"")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures { compose = true; buildConfig = true }
}
kotlin { compilerOptions { jvmTarget.set(JvmTarget.JVM_17) } }

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    implementation(libs.work.runtime.ktx)
    testImplementation(libs.junit)
}
