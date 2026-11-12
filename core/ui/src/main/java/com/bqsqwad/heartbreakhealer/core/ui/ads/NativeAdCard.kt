package com.bqsqwad.heartbreakhealer.core.ui.ads

import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
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
fun NativeAdCard(unitId: String, modifier: Modifier = Modifier) {
    var nativeAd by remember(unitId) { mutableStateOf<NativeAd?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current
    DisposableEffect(unitId) {
        val loader = AdLoader.Builder(context, unitId)
            .forNativeAd { loaded ->
                nativeAd?.destroy()
                nativeAd = loaded
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    nativeAd = null
                }
            })
            .build()
        loader.loadAd(AdRequest.Builder().build())
        onDispose {
            nativeAd?.destroy()
            nativeAd = null
        }
    }
    val ad = nativeAd ?: return
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
