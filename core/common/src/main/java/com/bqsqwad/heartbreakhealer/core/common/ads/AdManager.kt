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
import javax.inject.Inject
import javax.inject.Singleton

enum class InterstitialPlacement(val unitId: String) {
    SELF_CARE_COMPLETION(AdsConfig.INTERSTITIAL),
    PATH_DAY_COMPLETION(AdsConfig.INTERSTITIAL_PATH_DAY),
    WEEKLY_REVIEW_EXIT(AdsConfig.INTERSTITIAL_WEEKLY)
}

enum class RewardedPlacement(val unitId: String) {
    HEARTS_REWARD(AdsConfig.REWARDED_HEARTS),
    SHOP_EXTRA_REWARD(AdsConfig.REWARDED_SHOP_EXTRA)
}

@Singleton
class AdManager @Inject constructor() {

    private val interstitials = mutableMapOf<InterstitialPlacement, InterstitialAd?>()
    private val rewardedAds = mutableMapOf<RewardedPlacement, RewardedAd?>()
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
        InterstitialAd.load(
            context.applicationContext,
            placement.unitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitials[placement] = ad
                    Log.d(TAG, "Loaded interstitial placement: $placement")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
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
        RewardedAd.load(
            context.applicationContext,
            placement.unitId,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAds[placement] = ad
                    Log.d(TAG, "Loaded rewarded placement: $placement")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAds[placement] = null
                    Log.w(TAG, "Rewarded $placement failed to load: ${error.message}")
                }
            }
        )
    }

    /** Only call from an explicit user action; reward is granted solely by the SDK reward callback. */
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
        val ad = rewardedAds[placement]
        if (ad == null) {
            loadRewarded(activity, placement)
            onUnavailable()
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                isShowingFullScreenContent = true
            }

            override fun onAdDismissedFullScreenContent() {
                isShowingFullScreenContent = false
                rewardedAds[placement] = null
                loadRewarded(activity, placement)
                onDismissed()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                isShowingFullScreenContent = false
                rewardedAds[placement] = null
                Log.w(TAG, "Rewarded $placement failed to show: ${error.message}")
                loadRewarded(activity, placement)
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
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
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
