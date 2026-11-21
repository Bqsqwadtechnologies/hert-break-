package com.bqsqwad.heartbreakhealer.core.ui.ads

import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

/** Compact, clearly-labelled native placement for approved list screens only. */
@Composable
fun NativeAdCard(unitId: String, modifier: Modifier = Modifier, enabled: Boolean = true) {
    if (!enabled) return
    var nativeAd by remember(unitId) { mutableStateOf<NativeAd?>(null) }
    var loading by remember(unitId) { mutableStateOf(true) }
    val context = androidx.compose.ui.platform.LocalContext.current
    DisposableEffect(unitId) {
        var disposed = false
        val loader = AdLoader.Builder(context, unitId)
            .forNativeAd { loaded ->
                if (disposed) {
                    loaded.destroy()
                } else {
                    nativeAd = loaded
                    loading = false
                }
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    loading = false
                    Log.w(TAG, "Native ad failed for unit $unitId: code=${error.code}, domain=${error.domain}, message=${error.message}, response=${error.responseInfo}")
                    nativeAd = null
                }
            })
            .build()
        loader.loadAd(AdRequest.Builder().build())
        onDispose { disposed = true }
    }
    DisposableEffect(nativeAd) {
        onDispose { nativeAd?.destroy() }
    }
    val ad = nativeAd
    if (ad == null) {
        if (loading) {
            Card(modifier = modifier.fillMaxWidth()) {
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    Text(" Loading ad…", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        return
    }
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { viewContext ->
            val adView = NativeAdView(viewContext)
            val column = LinearLayout(viewContext).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(18, 14, 18, 14)
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            val disclosure = TextView(viewContext).apply { text = "Ad"; textSize = 11f }
            val headline = TextView(viewContext).apply { textSize = 16f; setTypeface(null, android.graphics.Typeface.BOLD) }
            val body = TextView(viewContext).apply { textSize = 14f }
            val cta = TextView(viewContext).apply { textSize = 14f; setPadding(0, 12, 0, 0) }
            column.addView(disclosure)
            column.addView(headline)
            column.addView(body)
            column.addView(cta)
            adView.addView(column)
            adView.headlineView = headline
            adView.bodyView = body
            adView.callToActionView = cta
            adView.tag = listOf(headline, body, cta)
            adView
        },
        update = { adView ->
            val views = adView.tag as? List<*>
            val headline = views?.getOrNull(0) as? TextView
            val body = views?.getOrNull(1) as? TextView
            val cta = views?.getOrNull(2) as? TextView
            headline?.text = ad.headline
            body?.text = ad.body.orEmpty()
            body?.visibility = if (ad.body.isNullOrBlank()) android.view.View.GONE else android.view.View.VISIBLE
            cta?.text = ad.callToAction.orEmpty()
            cta?.visibility = if (ad.callToAction.isNullOrBlank()) android.view.View.GONE else android.view.View.VISIBLE
            adView.setNativeAd(ad)
        }
    )
}

private const val TAG = "NativeAdCard"
