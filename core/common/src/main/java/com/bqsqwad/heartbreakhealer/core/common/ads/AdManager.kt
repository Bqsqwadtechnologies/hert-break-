package com.bqsqwad.heartbreakhealer.core.common.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdManager @Inject constructor() {

    private var interstitial: InterstitialAd? = null
    private var rewarded: RewardedAd? = null
    private var lastInterstitialShownAt = 0L
    private var interstitialsShownThisSession = 0
    private var consentResolved = false
    private var adsEnabled = true

    fun initialize(context: Context) {
        MobileAds.initialize(context) {
            Log.d(TAG, "MobileAds initialized")
        }
    }

    fun onConsentResolved(canShowAds: Boolean) {
        consentResolved = true
        adsEnabled = canShowAds
    }

    fun canShowInterstitial(): Boolean {
        if (!consentResolved || !adsEnabled) return false
        if (interstitialsShownThisSession >= AdsConfig.INTERSTITIAL_MAX_PER_SESSION) return false
        val elapsed = System.currentTimeMillis() - lastInterstitialShownAt
        return elapsed >= AdsConfig.INTERSTITIAL_MIN_INTERVAL_MS
    }

    fun loadInterstitial(context: Context) {
        if (!adsEnabled) return
        val request = AdRequest.Builder().build()
        InterstitialAd.load(context, AdsConfig.INTERSTITIAL, request, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) { interstitial = ad }
            override fun onAdFailedToLoad(error: LoadAdError) {
                interstitial = null
                Log.w(TAG, "Interstitial failed: ${error.message}")
            }
        })
    }

    fun showInterstitialIfAllowed(activity: Activity, onDismissed: () -> Unit = {}) {
        if (!canShowInterstitial()) {
            onDismissed()
            return
        }
        val ad = interstitial
        if (ad == null) {
            loadInterstitial(activity)
            onDismissed()
            return
        }
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitial = null
                lastInterstitialShownAt = System.currentTimeMillis()
                interstitialsShownThisSession++
                loadInterstitial(activity)
                onDismissed()
            }
            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                interstitial = null
                onDismissed()
            }
        }
        ad.show(activity)
    }

    fun loadRewarded(context: Context) {
        if (!adsEnabled) return
        val request = AdRequest.Builder().build()
        RewardedAd.load(context, AdsConfig.REWARDED_HEARTS, request, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedAd) { rewarded = ad }
            override fun onAdFailedToLoad(error: LoadAdError) { rewarded = null }
        })
    }

    fun showRewarded(activity: Activity, onRewarded: () -> Unit, onDismissed: () -> Unit = {}) {
        val ad = rewarded
        if (ad == null || !adsEnabled) {
            onDismissed()
            return
        }
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewarded = null
                loadRewarded(activity)
                onDismissed()
            }
            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                rewarded = null
                onDismissed()
            }
        }
        ad.show(activity) { onRewarded() }
    }

    companion object {
        private const val TAG = "AdManager"
    }
}
