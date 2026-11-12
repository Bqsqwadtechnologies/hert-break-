package com.bqsqwad.heartbreakhealer.feature.heartsshop

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bqsqwad.heartbreakhealer.core.common.ads.AdsConfig
import com.bqsqwad.heartbreakhealer.core.ui.ads.BannerAdView

@Composable
fun HeartsShopScreen(
    balance: Int = 50,
    onWatchRewarded: () -> Unit = {},
    onBack: () -> Unit = {},
    adsAllowed: Boolean = false
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Hearts Shop", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = onBack) { Text("Back") }
        }
        Spacer(Modifier.height(8.dp))
        Text("Balance: $balance hearts", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Earn more hearts", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Button(onClick = onWatchRewarded, modifier = Modifier.fillMaxWidth()) {
                    Text("Watch a short video to earn hearts")
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Coming soon: unlock themes, extra programs, affirmation packs.", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.weight(1f))
        // Dedicated banner unit, separated from purchases and reward controls.
        BannerAdView(unitId = AdsConfig.BANNER_HEARTS_SHOP, enabled = adsAllowed)
    }
}
