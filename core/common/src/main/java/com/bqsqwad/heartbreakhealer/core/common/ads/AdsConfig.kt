package com.bqsqwad.heartbreakhealer.core.common.ads

import com.bqsqwad.heartbreakhealer.core.common.BuildConfig

/**
 * One source of truth for all AdMob unit IDs.
 * Debug builds always use Google's official test units; release builds use the
 * production units configured for Heartbreak Healer.
 */
object AdsConfig {
    private const val TEST_BANNER = "ca-app-pub-3940256099942544/6300978111"
    private const val TEST_NATIVE = "ca-app-pub-3940256099942544/2247696110"
    private const val TEST_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712"
    private const val TEST_REWARDED = "ca-app-pub-3940256099942544/5224354917"
    private const val TEST_APP_OPEN = "ca-app-pub-3940256099942544/9257395921"

    private fun unit(test: String, production: String): String =
        if (BuildConfig.DEBUG) test else production

    // Banner placements
    val BANNER_HOME get() = unit(TEST_BANNER, "ca-app-pub-2748789567113929/3089925002")
    val BANNER_LIST get() = unit(TEST_BANNER, "ca-app-pub-2748789567113929/3102192435")
    val BANNER_LIBRARY get() = unit(TEST_BANNER, "ca-app-pub-2748789567113929/9564493151")
    val BANNER_PATHS get() = unit(TEST_BANNER, "ca-app-pub-2748789567113929/5625248140")
    val BANNER_HEARTS_SHOP get() = unit(TEST_BANNER, "ca-app-pub-2748789567113929/3611051218")

    // Native placements
    val NATIVE_AFFIRMATIONS get() = unit(TEST_NATIVE, "ca-app-pub-2748789567113929/1789110768")
    val NATIVE_REBUILD get() = unit(TEST_NATIVE, "ca-app-pub-2748789567113929/2336080683")
    val NATIVE_LIBRARY get() = unit(TEST_NATIVE, "ca-app-pub-2748789567113929/4336217202")
    val NATIVE_PATHS get() = unit(TEST_NATIVE, "ca-app-pub-2748789567113929/6591020463")
    val NATIVE_WEEKLY_REVIEW get() = unit(TEST_NATIVE, "ca-app-pub-2748789567113929/8089498068")

    // Full-screen placements
    val INTERSTITIAL get() = unit(TEST_INTERSTITIAL, "ca-app-pub-2748789567113929/7820166648")
    val INTERSTITIAL_PATH_DAY get() = unit(TEST_INTERSTITIAL, "ca-app-pub-2748789567113929/1318191688")
    val INTERSTITIAL_WEEKLY get() = unit(TEST_INTERSTITIAL, "ca-app-pub-2748789567113929/6399448775")

    // Rewarded placements
    val REWARDED_HEARTS get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/1334649343")
    val REWARDED_SHOP_EXTRA get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/4144645513")
    val REWARDED_HEALING_PATH_14 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/6182423982")
    val REWARDED_REBUILD_30 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/8617015632")
    val REWARDED_STORY_PART_1 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/9340913674")
    val REWARDED_STORY_PART_2 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/6523178648")
    val REWARDED_STORY_PART_3 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/3897015305")
    val REWARDED_STORY_PART_4 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/8498430286")
    val REWARDED_STORY_PART_5 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/8953900862")
    val REWARDED_STORY_PART_6 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/6866618569")
    val REWARDED_STORY_PART_7 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/3152790171")
    val REWARDED_STORY_PART_8 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/6653051487")
    val REWARDED_STORY_PART_9 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/9249239322")
    val REWARDED_STORY_PART_10 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/6463468890")
    val REWARDED_STORY_PART_11 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/3174785562")
    val REWARDED_STORY_PART_12 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/5014655855")
    val REWARDED_STORY_PART_13 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/1235192946")
    val REWARDED_STORY_PART_14 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/3996912643")
    val REWARDED_STORY_PART_15 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/3837305556")
    val REWARDED_STORY_PART_16 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/1839708502")
    val REWARDED_STORY_PART_17 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/9057667638")
    val REWARDED_STORY_PART_18 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/9548622220")
    val REWARDED_STORY_PART_19 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/8235540550")
    val REWARDED_STORY_PART_20 get() = unit(TEST_REWARDED, "ca-app-pub-2748789567113929/7988128544")

    // App-open inventory is reserved; no app-open format is currently shown.
    val APP_OPEN get() = unit(TEST_APP_OPEN, "ca-app-pub-2748789567113929/7628594951")

    // Conservative full-screen frequency caps.
    const val INTERSTITIAL_MIN_INTERVAL_MS = 3 * 60 * 1000L
    const val INTERSTITIAL_MAX_PER_SESSION = 3
    const val APP_OPEN_MIN_BACKGROUND_MS = 30 * 60 * 1000L
    const val APP_OPEN_MAX_AD_AGE_MS = 4 * 60 * 60 * 1000L
}
