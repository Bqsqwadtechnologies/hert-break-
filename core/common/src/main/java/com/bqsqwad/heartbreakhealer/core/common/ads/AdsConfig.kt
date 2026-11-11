package com.bqsqwad.heartbreakhealer.core.common.ads

/**
 * Single place for all AdMob unit IDs.
 * Production IDs — BQ Sqwad Technologies / Heartbreak Healer.
 */
object AdsConfig {
    // App ID: ca-app-pub-2748789567113929~6610046816

    // Banners
    const val BANNER_HOME = "ca-app-pub-2748789567113929/3089925002"
    const val BANNER_LIST = "ca-app-pub-2748789567113929/3102192435"
    const val BANNER_LIBRARY = "ca-app-pub-2748789567113929/9564493151"
    const val BANNER_PATHS = "ca-app-pub-2748789567113929/5625248140"

    // Native
    const val NATIVE_AFFIRMATIONS = "ca-app-pub-2748789567113929/1789110768"
    const val NATIVE_REBUILD = "ca-app-pub-2748789567113929/2336080683"
    const val NATIVE_LIBRARY = "ca-app-pub-2748789567113929/4336217202"
    const val NATIVE_PATHS = "ca-app-pub-2748789567113929/6591020463"

    // Interstitial
    const val INTERSTITIAL = "ca-app-pub-2748789567113929/7820166648"
    const val INTERSTITIAL_PATH_DAY = "ca-app-pub-2748789567113929/1318191688"
    const val INTERSTITIAL_WEEKLY = "ca-app-pub-2748789567113929/6399448775"

    // Rewarded
    const val REWARDED_HEARTS = "ca-app-pub-2748789567113929/1334649343"
    const val REWARDED_SHOP_EXTRA = "ca-app-pub-2748789567113929/4144645513"

    // App Open
    const val APP_OPEN = "ca-app-pub-2748789567113929/7628594951"

    // Frequency caps
    const val INTERSTITIAL_MIN_INTERVAL_MS = 3 * 60 * 1000L
    const val INTERSTITIAL_MAX_PER_SESSION = 3
    const val APP_OPEN_MIN_BACKGROUND_MS = 4 * 60 * 60 * 1000L
}
