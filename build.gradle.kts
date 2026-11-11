plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // Keep Hilt and KSP plugin implementations in the same root plugin classloader.
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}
