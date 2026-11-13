package com.bqsqwad.heartbreakhealer.core.common.ads

/**
 * Auditable inventory of the 16 production AdMob units and their app placements.
 * Ads stay out of sensitive recovery, privacy, and support flows.
 */
object AdPlacementMap {
    val BANNER_SCREENS = listOf(
        "BANNER_HOME — Dashboard bottom",
        "BANNER_LIST — Self-Care, 30-Day Rebuild, and Affirmations bottom",
        "BANNER_LIBRARY — Healing Library, Journal list, Unsent Letters list, Memories, Triggers, and No-Contact screen footer",
        "BANNER_PATHS — 14-Day Healing Path bottom",
        "BANNER_HEARTS_SHOP — Hearts Shop bottom, separated from rewards"
    )

    val NATIVE_SCREENS = listOf(
        "NATIVE_AFFIRMATIONS — after every 8 affirmations",
        "NATIVE_REBUILD — after every 8 completed-program items",
        "NATIVE_LIBRARY — within the article list at the designated interval",
        "NATIVE_PATHS — within the 14-day path at the designated interval",
        "NATIVE_WEEKLY_REVIEW — below the weekly progress summary"
    )

    val INTERSTITIAL_TRIGGERS = listOf(
        "INTERSTITIAL — after a Self-Care task completion",
        "INTERSTITIAL_PATH_DAY — after a 14-Day Healing Path day completion",
        "INTERSTITIAL_WEEKLY — when the user finishes and exits Weekly Review"
    )

    val REWARDED_TRIGGERS = listOf(
        "REWARDED_HEARTS — user-initiated Hearts Shop video; grant hearts only from the SDK reward callback",
        "REWARDED_SHOP_EXTRA — separate optional user-initiated extra video; grant hearts only from the SDK reward callback"
    )

    const val APP_OPEN_TRIGGER =
        "APP_OPEN — eligible return after at least 30 minutes in background; never on cold start or sensitive screens"

    val NEVER_SHOW_ADS = listOf(
        "Onboarding",
        "Journal and Unsent Letters",
        "Urge SOS and coping flows",
        "No-Contact and slip recovery",
        "Memories",
        "Support / crisis information",
        "Settings, privacy and legal screens",
        "Permission, loading and error screens"
    )

    const val INTERSTITIAL_MIN_INTERVAL_MS = 3 * 60 * 1000L
    const val INTERSTITIAL_MAX_PER_SESSION = 3
    const val APP_OPEN_MIN_BACKGROUND_MS = 30 * 60 * 1000L
    const val APP_OPEN_MAX_AD_AGE_MS = 4 * 60 * 60 * 1000L
}
