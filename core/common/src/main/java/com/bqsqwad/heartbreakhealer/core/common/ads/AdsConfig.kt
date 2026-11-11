package com.bqsqwad.heartbreakhealer.core.common.ads

/**
 * Single place for all AdMob unit IDs.
 * Real production IDs from AdMob console (BQ Sqwad Technologies).
 * Never hard-code these IDs anywhere else in the app.
 */
object AdsConfig {
    // App ID (also in AndroidManifest)
    // ca-app-pub-2748789567113929~6610046816

    // Banner (adaptive)
    const val BANNER_HOME = "ca-app-pub-2748789567113929/3089925002"
    const val BANNER_LIST = "ca-app-pub-2748789567113929/3102192435"

    // Native
    const val NATIVE_AFFIRMATIONS = "ca-app-pub-2748789567113929/1789110768"
    const val NATIVE_REBUILD = "ca-app-pub-2748789567113929/2336080683"

    // Interstitial (capped)
    const val INTERSTITIAL = "ca-app-pub-2748789567113929/7820166648"

    // Rewarded (hearts only)
    const val REWARDED_HEARTS = "ca-app-pub-2748789567113929/1334649343"

    // App Open (capped)
    const val APP_OPEN = "ca-app-pub-2748789567113929/7628594951"

    // Frequency caps
    const val INTERSTITIAL_MIN_INTERVAL_MS = 3 * 60 * 1000L // 3 minutes
    const val INTERSTITIAL_MAX_PER_SESSION = 3
    const val APP_OPEN_MIN_BACKGROUND_MS = 4 * 60 * 60 * 1000L // 4 hours
}
