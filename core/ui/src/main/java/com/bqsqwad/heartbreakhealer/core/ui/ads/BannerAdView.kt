package com.bqsqwad.heartbreakhealer.core.ui.ads

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

/**
 * Adaptive banner. Pass unitId from AdsConfig (BANNER_HOME or BANNER_LIST).
 * Only use on allowed screens (Dashboard, Self-care, Rebuild, Affirmations).
 */
@Composable
fun BannerAdView(
    unitId: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    if (!enabled) return
    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = unitId
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
