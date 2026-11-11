package com.bqsqwad.heartbreakhealer.core.common.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Google UMP consent for EEA/UK. Call requestConsent before showing personalized ads.
 * AdManager should only serve ads after onConsentResolved is called.
 */
@Singleton
class ConsentHelper @Inject constructor() {

    private var consentInformation: ConsentInformation? = null

    fun requestConsent(
        activity: Activity,
        onComplete: (canShowAds: Boolean) -> Unit
    ) {
        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()
        val consentInfo = UserMessagingPlatform.getConsentInformation(activity)
        consentInformation = consentInfo
        consentInfo.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    if (formError != null) {
                        Log.w(TAG, "Consent form error: ${formError.message}")
                    }
                    val canShow = consentInfo.canRequestAds()
                    onComplete(canShow)
                }
            },
            { error ->
                Log.w(TAG, "Consent info update failed: ${error.message}")
                onComplete(true) // fail-open for non-EEA / error
            }
        )
    }

    fun canRequestAds(context: Context): Boolean {
        return consentInformation?.canRequestAds()
            ?: UserMessagingPlatform.getConsentInformation(context).canRequestAds()
    }

    companion object {
        private const val TAG = "ConsentHelper"
    }
}
