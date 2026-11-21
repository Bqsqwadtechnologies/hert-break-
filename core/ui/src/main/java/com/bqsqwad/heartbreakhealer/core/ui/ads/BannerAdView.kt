package com.bqsqwad.heartbreakhealer.core.ui.ads

import android.util.Log
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

/**
 * Adaptive anchored banner. Width is measured from the actual placement (not the
 * full device width), and the AdView is destroyed when that placement leaves composition.
 */
@Composable
fun BannerAdView(
    unitId: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    if (!enabled || unitId.isBlank()) return

    val context = LocalContext.current
    val density = LocalDensity.current
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val widthDp = maxWidth.value.toInt().coerceAtLeast(1)
        val adView = remember(unitId, widthDp) {
            AdView(context).apply {
                adUnitId = unitId
                setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, widthDp))
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        Log.d(TAG, "Banner loaded for unit $unitId")
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.w(TAG, "Banner failed for unit $unitId: code=${error.code}, domain=${error.domain}, message=${error.message}, response=${error.responseInfo}")
                    }
                }
                loadAd(AdRequest.Builder().build())
            }
        }
        val bannerHeight = with(density) {
            (adView.adSize?.getHeightInPixels(context) ?: 50).toDp()
        }

        DisposableEffect(adView) {
            onDispose { adView.destroy() }
        }

        AndroidView(
            modifier = Modifier.fillMaxWidth().height(bannerHeight),
            factory = { adView },
            update = { /* Do not issue a new request during recomposition. */ }
        )
    }
}

private const val TAG = "BannerAdView"
