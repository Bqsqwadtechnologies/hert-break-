package com.bqsqwad.heartbreakhealer.feature.rebuild

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bqsqwad.heartbreakhealer.core.common.ads.AdsConfig
import com.bqsqwad.heartbreakhealer.core.ui.ads.BannerAdView
import com.bqsqwad.heartbreakhealer.core.ui.ads.NativeAdCard

data class RebuildDayUi(val day: Int, val title: String, val category: String, val done: Boolean)

@Composable
fun RebuildScreen(
    days: List<RebuildDayUi> = (1..30).map {
        RebuildDayUi(it, "Day $it challenge", listOf("Fitness","Hobby","Social","Confidence","Finance")[it % 5], false)
    },
    completedDays: Set<Int> = emptySet(),
    onCompleteDay: (Int) -> Unit = {},
    adsAllowed: Boolean = false,
    onBack: () -> Unit = {}
) {
    val dayState = remember(days, completedDays) { mutableStateListOf<RebuildDayUi>().apply { addAll(days.map { it.copy(done = it.day in completedDays) }) } }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("30-Day Rebuild", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = onBack) { Text("Back") }
        }
        Text("Build confidence through one manageable action each day.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(10.dp))
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(Modifier.padding(14.dp)) {
                Text("${dayState.count { it.done }} / 30 days completed", style = MaterialTheme.typography.titleMedium)
                LinearProgressIndicator(progress = { dayState.count { it.done } / 30f }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
            }
        }
        Spacer(Modifier.height(10.dp))
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(dayState, key = { it.day }) { day ->
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Row(Modifier.padding(13.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("Day ${day.day}: ${day.title}", style = MaterialTheme.typography.titleMedium)
                            Text(day.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        if (day.done) Text("Completed ✓", color = MaterialTheme.colorScheme.primary)
                        else Button(onClick = {
                            val index = dayState.indexOfFirst { it.day == day.day }
                            if (index >= 0) dayState[index] = day.copy(done = true)
                            onCompleteDay(day.day)
                        }) { Text("Complete") }
                    }
                }
                if (day.day % 8 == 0) NativeAdCard(unitId = AdsConfig.NATIVE_REBUILD, enabled = adsAllowed)
            }
        }
        Text("Support this free healing space", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        BannerAdView(unitId = AdsConfig.BANNER_LIST, enabled = adsAllowed)
    }
}
