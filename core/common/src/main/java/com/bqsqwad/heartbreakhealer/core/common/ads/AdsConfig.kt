package com.bqsqwad.heartbreakhealer.core.common.ads

/**
 * Single place for all AdMob unit IDs.
 * Currently using Google test IDs. Replace with real IDs from AdMob console later.
 * Never hard-code real IDs anywhere else.
 */
object AdsConfig {
    // Banner (adaptive)
    const val BANNER_HOME = "ca-app-pub-3940256099942544/6300978111"
    const val BANNER_LIST = "ca-app-pub-3940256099942544/6300978111"

    // Native
    const val NATIVE_AFFIRMATIONS = "ca-app-pub-3940256099942544/2247696110"
    const val NATIVE_REBUILD = "ca-app-pub-3940256099942544/2247696110"

    // Interstitial (capped)
    const val INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712"

    // Rewarded (hearts only)
    const val REWARDED_HEARTS = "ca-app-pub-3940256099942544/5224354917"

    // App Open (capped)
    const val APP_OPEN = "ca-app-pub-3940256099942544/9257395921"

    // Frequency caps
    const val INTERSTITIAL_MIN_INTERVAL_MS = 3 * 60 * 1000L // 3 minutes
    const val INTERSTITIAL_MAX_PER_SESSION = 3
    const val APP_OPEN_MIN_BACKGROUND_MS = 4 * 60 * 60 * 1000L // 4 hours
}
