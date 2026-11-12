package com.bqsqwad.heartbreakhealer.core.common.ads

/**
 * Current code-backed ad inventory. Ads remain outside sensitive recovery flows.
 */
object AdPlacementMap {
    val BANNER_SCREENS = listOf(
        "Dashboard home — bottom of scroll content",
        "Self-Care — bottom of task list",
        "30-Day Rebuild — bottom of challenge list",
        "Affirmations — bottom of list",
        "Healing Library — bottom of article list",
        "14-Day Healing Path — bottom of path list",
        "Hearts Shop — bottom, separated from reward and purchase controls"
    )

    val NATIVE_SCREENS = listOf(
        "Affirmations — after every 8 affirmations",
        "30-Day Rebuild — after every 8 challenges",
        "Healing Library — after every 6 articles",
        "14-Day Healing Path — after every 6 days",
        "Weekly Review — below the progress summary"
    )

    val INTERSTITIAL_TRIGGERS = listOf(
        "After completing a Self-Care task, subject to frequency cap",
        "After completing a 30-Day Rebuild challenge, subject to frequency cap",
        "After completing a 14-Day Healing Path day, subject to frequency cap"
    )

    val REWARDED_TRIGGERS = listOf(
        "Hearts Shop — user-initiated reward video for bonus hearts"
    )

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
    const val APP_OPEN_STATUS = "Reserved only; not active"

    /** Suggested next placements; create only after owner approval and separate AdMob IDs. */
    val PROPOSED_PLACEMENTS = listOf(
        "A single native card at the end of the Healing Tools overview, outside SOS and crisis content",
        "A single native card at the end of the completed-program catalogue, away from completion controls",
        "A single banner at the bottom of the Memories overview only if the screen is informational and not emotionally sensitive"
    )
}
