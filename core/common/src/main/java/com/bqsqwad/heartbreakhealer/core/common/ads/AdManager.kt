package com.bqsqwad.heartbreakhealer.core.common.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton

enum class InterstitialPlacement(val unitId: String) {
    SELF_CARE_COMPLETION(AdsConfig.INTERSTITIAL),
    PATH_DAY_COMPLETION(AdsConfig.INTERSTITIAL_PATH_DAY),
    WEEKLY_REVIEW_EXIT(AdsConfig.INTERSTITIAL_WEEKLY)
}

enum class RewardedPlacement(val unitId: String) {
    HEARTS_REWARD(AdsConfig.REWARDED_HEARTS),
    SHOP_EXTRA_REWARD(AdsConfig.REWARDED_SHOP_EXTRA),
    HEALING_PATH_14(AdsConfig.REWARDED_HEALING_PATH_14),
    REBUILD_30(AdsConfig.REWARDED_REBUILD_30),
    STORY_UNLOCK(AdsConfig.REWARDED_HEARTS),
    JOKE_UNLOCK(AdsConfig.REWARDED_SHOP_EXTRA),
    STORY_PART_1(AdsConfig.REWARDED_STORY_PART_1),
    STORY_PART_2(AdsConfig.REWARDED_STORY_PART_2),
    STORY_PART_3(AdsConfig.REWARDED_STORY_PART_3),
    STORY_PART_4(AdsConfig.REWARDED_STORY_PART_4),
    STORY_PART_5(AdsConfig.REWARDED_STORY_PART_5),
    STORY_PART_6(AdsConfig.REWARDED_STORY_PART_6),
    STORY_PART_7(AdsConfig.REWARDED_STORY_PART_7),
    STORY_PART_8(AdsConfig.REWARDED_STORY_PART_8),
    STORY_PART_9(AdsConfig.REWARDED_STORY_PART_9),
    STORY_PART_10(AdsConfig.REWARDED_STORY_PART_10),
    STORY_PART_11(AdsConfig.REWARDED_STORY_PART_11),
    STORY_PART_12(AdsConfig.REWARDED_STORY_PART_12),
    STORY_PART_13(AdsConfig.REWARDED_STORY_PART_13),
    STORY_PART_14(AdsConfig.REWARDED_STORY_PART_14),
    STORY_PART_15(AdsConfig.REWARDED_STORY_PART_15),
    STORY_PART_16(AdsConfig.REWARDED_STORY_PART_16),
    STORY_PART_17(AdsConfig.REWARDED_STORY_PART_17),
    STORY_PART_18(AdsConfig.REWARDED_STORY_PART_18),
    STORY_PART_19(AdsConfig.REWARDED_STORY_PART_19),
    STORY_PART_20(AdsConfig.REWARDED_STORY_PART_20);

    companion object {
        fun storyPart(partNumber: Int): RewardedPlacement {
            val index = ((partNumber - 1).coerceAtLeast(0) % 20) + 1
            return values().first { it.name == "STORY_PART_$index" }
        }
    }
}

@Singleton
class AdManager @Inject constructor() {

    private val interstitials = mutableMapOf<InterstitialPlacement, InterstitialAd?>()
    private val rewardedAds = mutableMapOf<RewardedPlacement, RewardedAd?>()
    private val loadingRewardedPlacements = Collections.synchronizedSet(mutableSetOf<RewardedPlacement>())
    private val loadingInterstitials = Collections.synchronizedSet(mutableSetOf<InterstitialPlacement>())
    private var appOpenAd: AppOpenAd? = null
    private var appOpenLoadedAt = 0L
    private var lastInterstitialShownAt = 0L
    private var interstitialsShownThisSession = 0
    private var consentResolved = false
    private var adsEnabled = false
    private var initialized = false
    private var isShowingFullScreenContent = false
    private var backgroundedAt = 0L
    private var appOpenEligible = false
    private var eligibleForegroundCount = 0

    private val _adsAllowed = MutableStateFlow(false)
    val adsAllowed: StateFlow<Boolean> = _adsAllowed

    /** Call once after the consent SDK has resolved the user's ad eligibility. */
    fun onConsentResolved(context: Context, canShowAds: Boolean) {
        consentResolved = true
        adsEnabled = canShowAds
        _adsAllowed.value = canShowAds
        if (!canShowAds || initialized) return

        initialized = true
        MobileAds.initialize(context.applicationContext) {
            val appContext = context.applicationContext
            InterstitialPlacement.values().forEach { loadInterstitial(appContext, it) }
            RewardedPlacement.values().forEach { loadRewarded(appContext, it) }
            loadAppOpen(appContext)
            Log.d(TAG, "Mobile Ads initialized; all registered full-screen placements requested")
        }
    }

    fun canShowInterstitial(): Boolean {
        if (!canRequestAds()) return false
        if (isShowingFullScreenContent) return false
        if (interstitialsShownThisSession >= AdsConfig.INTERSTITIAL_MAX_PER_SESSION) return false
        return System.currentTimeMillis() - lastInterstitialShownAt >= AdsConfig.INTERSTITIAL_MIN_INTERVAL_MS
    }

    fun loadInterstitial(context: Context, placement: InterstitialPlacement) {
        if (!canRequestAds()) return
        if (interstitials[placement] != null) return
        if (!loadingInterstitials.add(placement)) return
        InterstitialAd.load(
            context.applicationContext,
            placement.unitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    loadingInterstitials.remove(placement)
                    interstitials[placement] = ad
                    Log.d(TAG, "Loaded interstitial placement: $placement")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    loadingInterstitials.remove(placement)
                    interstitials[placement] = null
                    Log.w(TAG, "Interstitial $placement failed to load: ${error.message}")
                }
            }
        )
    }

    fun showInterstitialIfAllowed(
        activity: Activity,
        placement: InterstitialPlacement,
        onDismissed: () -> Unit = {}
    ) {
        if (!canShowInterstitial()) {
            onDismissed()
            return
        }
        val ad = interstitials[placement]
        if (ad == null) {
            loadInterstitial(activity, placement)
            onDismissed()
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                isShowingFullScreenContent = true
                lastInterstitialShownAt = System.currentTimeMillis()
                interstitialsShownThisSession++
            }

            override fun onAdDismissedFullScreenContent() {
                isShowingFullScreenContent = false
                interstitials[placement] = null
                loadInterstitial(activity, placement)
                onDismissed()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                isShowingFullScreenContent = false
                interstitials[placement] = null
                Log.w(TAG, "Interstitial $placement failed to show: ${error.message}")
                loadInterstitial(activity, placement)
                onDismissed()
            }
        }
        ad.show(activity)
    }

    fun loadRewarded(context: Context, placement: RewardedPlacement) {
        if (!canRequestAds()) return
        if (rewardedAds[placement] != null) return
        if (!loadingRewardedPlacements.add(placement)) return
        RewardedAd.load(
            context.applicationContext,
            placement.unitId,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    loadingRewardedPlacements.remove(placement)
                    rewardedAds[placement] = ad
                    Log.d(TAG, "Loaded rewarded placement: $placement")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    loadingRewardedPlacements.remove(placement)
                    rewardedAds[placement] = null
                    Log.w(TAG, "Rewarded $placement failed to load: ${error.message}")
                }
            }
        )
    }

    /** Rewarded video is only opened after the user explicitly chooses the watch action. */
    fun showRewarded(
        activity: Activity,
        placement: RewardedPlacement,
        onRewarded: () -> Unit,
        onDismissed: () -> Unit = {},
        onUnavailable: () -> Unit = {}
    ) {
        if (!canRequestAds() || isShowingFullScreenContent) {
            onUnavailable()
            return
        }
        var ad = rewardedAds.remove(placement)
        var consumedPlacement = placement

        // Check if ANY preloaded rewarded ad is already in cache
        if (ad == null) {
            val alternate = rewardedAds.entries.firstOrNull { it.value != null }
            if (alternate != null) {
                ad = alternate.value
                consumedPlacement = alternate.key
                rewardedAds.remove(alternate.key)
            }
        }

        if (ad != null) {
            loadRewarded(activity.applicationContext, consumedPlacement)
            showLoadedRewarded(activity, consumedPlacement, ad, onRewarded, onDismissed, onUnavailable)
            return
        }

        // If no ad is cached right now, do not block or delay:
        // Preload in the background so the next action is primed, and immediately continue.
        loadRewarded(activity.applicationContext, placement)
        onUnavailable()
    }

    private fun showLoadedRewarded(
        activity: Activity,
        placement: RewardedPlacement,
        ad: RewardedAd,
        onRewarded: () -> Unit,
        onDismissed: () -> Unit,
        onUnavailable: () -> Unit
    ) {
        if (activity.isFinishing || activity.isDestroyed) {
            onUnavailable()
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                isShowingFullScreenContent = true
            }

            override fun onAdDismissedFullScreenContent() {
                isShowingFullScreenContent = false
                loadRewarded(activity.applicationContext, placement)
                onDismissed()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                isShowingFullScreenContent = false
                Log.w(TAG, "Rewarded $placement failed to show: ${error.message}")
                loadRewarded(activity.applicationContext, placement)
                onUnavailable()
                onDismissed()
            }
        }
        ad.show(activity) { onRewarded() }
    }

    private fun loadAppOpen(context: Context) {
        if (!canRequestAds()) return
        AppOpenAd.load(
            context.applicationContext,
            AdsConfig.APP_OPEN,
            AdRequest.Builder().build(),
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    appOpenLoadedAt = System.currentTimeMillis()
                    Log.d(TAG, "Loaded app-open ad")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    appOpenAd = null
                    Log.w(TAG, "App-open ad failed to load: ${error.message}")
                }
            }
        )
    }

    /** Called by MainActivity onStart. The first cold launch never interrupts onboarding. */
    fun setAppOpenEligible(eligible: Boolean) {
        appOpenEligible = eligible
    }

    fun onAppForeground(activity: Activity) {
        if (!canRequestAds() || isShowingFullScreenContent) return
        if (!appOpenEligible) {
            backgroundedAt = 0L
            return
        }
        eligibleForegroundCount++
        val elapsedInBackground = if (backgroundedAt == 0L) 0L else System.currentTimeMillis() - backgroundedAt
        if (backgroundedAt == 0L || elapsedInBackground < AdsConfig.APP_OPEN_MIN_BACKGROUND_MS) {
            backgroundedAt = 0L
            return
        }
        // Let users become familiar with the app before the first app-open ad.
        if (eligibleForegroundCount < 3) {
            backgroundedAt = 0L
            return
        }
        backgroundedAt = 0L
        val ad = appOpenAd
        val adAge = System.currentTimeMillis() - appOpenLoadedAt
        if (ad == null || adAge >= AdsConfig.APP_OPEN_MAX_AD_AGE_MS) {
            appOpenAd = null
            appOpenLoadedAt = 0L
            loadAppOpen(activity)
            return
        }
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                isShowingFullScreenContent = true
                appOpenAd = null
                appOpenLoadedAt = 0L
            }

            override fun onAdDismissedFullScreenContent() {
                isShowingFullScreenContent = false
                loadAppOpen(activity)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                isShowingFullScreenContent = false
                appOpenAd = null
                appOpenLoadedAt = 0L
                Log.w(TAG, "App-open ad failed to show: ${error.message}")
                loadAppOpen(activity)
            }
        }
        ad.show(activity)
    }

    /** Called by MainActivity onStop; full-screen ad transitions are not treated as app exits. */
    fun onAppBackground() {
        if (isShowingFullScreenContent || !canRequestAds()) return
        backgroundedAt = System.currentTimeMillis()
    }

    private fun canRequestAds(): Boolean = consentResolved && adsEnabled

    companion object {
        private const val TAG = "AdManager"
    }
}
