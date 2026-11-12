package com.bqsqwad.heartbreakhealer.core.common.ads

/**
 * Current, code-backed ad placement inventory.
 *
 * Ads are intentionally excluded from onboarding, Journal/Unsent Letters,
 * Urge SOS, No-Contact, Memories, crisis/support, Settings, permissions and errors.
 */
object AdPlacementMap {
    val BANNER_SCREENS = listOf(
        "Dashboard home — bottom of scroll content",
        "Self-Care — bottom of task list",
        "30-Day Rebuild — bottom of challenge list",
        "Affirmations — bottom of list",
        "Healing Library — bottom of article list",
        "14-Day Healing Path — bottom of path list",
        "Weekly Review — bottom of summary"
    )

    val NATIVE_SCREENS = listOf(
        "Affirmations — after every 8 affirmations",
        "30-Day Rebuild — after every 8 challenges",
        "Healing Library — after every 6 articles",
        "14-Day Healing Path — after every 6 days"
    )

    val INTERSTITIAL_TRIGGERS = listOf(
        "After completing a Self-Care task",
        "After completing a 30-Day Rebuild challenge",
        "After completing a 14-Day Healing Path day"
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

    /** Optional inventory to create only after the owner approves additional placements. */
    val PROPOSED_PLACEMENTS = listOf(
        "A single native card near the end of the Weekly Review (not inside progress metrics)",
        "One optional native card at the end of the Hearts Shop, visually separated from purchase and reward controls"
    )

    /** App-open ads are not currently implemented or shown. */
    const val APP_OPEN_STATUS = "Reserved only; not active"
}
