package com.bqsqwad.heartbreakhealer.core.common.ads

/**
 * Exact ad placement rules for Heartbreak Healer.
 *
 * NEVER show ads on:
 * - Onboarding
 * - Journal / Unsent letters
 * - Urge SOS (breathing, distraction, grounding, dont-text)
 * - No-contact slip flow
 * - Memories box
 * - Crisis / "Need more support?"
 * - Settings / legal / privacy
 * - Permission or error screens
 */
object AdPlacementMap {

    val BANNER_SCREENS = listOf(
        "Dashboard (home)",
        "Self-care planner list",
        "30-day rebuild program list",
        "Affirmations list"
    )

    val NATIVE_SCREENS = listOf(
        "Affirmations list (every 8th item)",
        "Rebuild program list (every 8th item)"
    )

    val INTERSTITIAL_TRIGGERS = listOf(
        "After completing a self-care task",
        "After completing a daily rebuild challenge"
    )

    val REWARDED_TRIGGERS = listOf(
        "Hearts Shop – Watch a short video to earn hearts",
        "Top-bar hearts balance – optional Watch to earn"
    )

    val APP_OPEN_RULE = "Only on return from background ≥ 4 hours, once per session, never first launch"

    /** Recommended names to create in AdMob console */
    val RECOMMENDED_UNIT_NAMES = mapOf(
        "Banner" to listOf("hh_banner_home", "hh_banner_lists"),
        "Native" to listOf("hh_native_affirmations", "hh_native_rebuild"),
        "Interstitial" to listOf("hh_interstitial_task_complete"),
        "Rewarded" to listOf("hh_rewarded_hearts"),
        "App Open" to listOf("hh_app_open")
    )
}
