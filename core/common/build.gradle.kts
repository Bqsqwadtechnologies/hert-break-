plugins {
    alias(libs.plugins.android.library)
}
android {
    namespace = "com.bqsqwad.heartbreakhealer.core.common"
    compileSdk = 36
    defaultConfig { minSdk = 26 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.play.services.ads)
    implementation(libs.user.messaging.platform)
    implementation(libs.hilt.android)
    implementation(libs.androidx.biometric)
}
