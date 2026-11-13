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
import com.bqsqwad.heartbreakhealer.core.ui.components.JourneyRewardOfferDialog
import com.bqsqwad.heartbreakhealer.core.ui.components.JourneySuccessDialog

data class RebuildDayUi(val day: Int, val title: String, val category: String, val done: Boolean)

@Composable
fun RebuildScreen(
    days: List<RebuildDayUi> = (1..30).map {
        RebuildDayUi(it, "Day $it challenge", listOf("Fitness", "Hobby", "Social", "Confidence", "Finance")[it % 5], false)
    },
    completedDays: Set<Int> = emptySet(),
    completedToday: Boolean = false,
    onCompleteDay: (Int, (Boolean) -> Unit) -> Unit = { _, result -> result(false) },
    onWatchRewarded: ((() -> Unit) -> Unit) = { finished -> finished() },
    adsAllowed: Boolean = false,
    onBack: () -> Unit = {}
) {
    var completedLocally by remember { mutableStateOf(emptySet<Int>()) }
    var blockedToday by remember(completedToday) { mutableStateOf(completedToday) }
    var showRewardOffer by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    val completed = completedDays + completedLocally
    val nextDay = (1..30).firstOrNull { it !in completed } ?: 31

    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("30-Day Rebuild", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = onBack) { Text("Back") }
        }
        Text("Build confidence through one manageable action each day.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(10.dp))
        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(Modifier.padding(14.dp)) {
                Text("${completed.size.coerceAtMost(30)} / 30 days completed", style = MaterialTheme.typography.titleMedium)
                LinearProgressIndicator(
                    progress = { completed.size.coerceAtMost(30) / 30f },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
        }
        if (blockedToday && nextDay <= 30) {
            Spacer(Modifier.height(8.dp))
            Text(
                "You've completed your one journey task for today. Your next task unlocks tomorrow.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.height(10.dp))
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(days, key = { it.day }) { day ->
                val isDone = day.done || day.day in completed
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Row(
                        Modifier.padding(13.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Day ${day.day}: ${day.title}", style = MaterialTheme.typography.titleMedium)
                            Text(day.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        when {
                            isDone -> Text("Completed ✓", color = MaterialTheme.colorScheme.primary)
                            day.day != nextDay -> Text("Locked", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            blockedToday -> Text("Tomorrow", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            else -> Button(onClick = {
                                onCompleteDay(day.day) { success ->
                                    if (success) {
                                        completedLocally = completedLocally + day.day
                                        blockedToday = true
                                        showRewardOffer = true
                                    } else {
                                        blockedToday = true
                                    }
                                }
                            }) { Text("Complete") }
                        }
                    }
                }
                if (day.day % 8 == 0) NativeAdCard(unitId = AdsConfig.NATIVE_REBUILD, enabled = adsAllowed)
            }
        }
        Text("Support this free healing space", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        BannerAdView(unitId = AdsConfig.BANNER_LIST, enabled = adsAllowed)
    }

    if (showRewardOffer) {
        JourneyRewardOfferDialog(
            title = "A strong step forward",
            description = "Your Rebuild task is safely saved. Watching is optional: you can watch a short video to support the app and earn 15 Hearts, or continue without an ad.",
            showRewardedOption = adsAllowed,
            onWatchRewarded = {
                onWatchRewarded {
                    showRewardOffer = false
                    showSuccess = true
                }
            },
            onContinue = {
                showRewardOffer = false
                showSuccess = true
            }
        )
    }
    if (showSuccess) {
        JourneySuccessDialog(
            title = "Keep rebuilding. You're doing well!",
            message = "You showed up for yourself today, and that matters. Keep believing in the person you're becoming. Be proud of this step, take tomorrow gently, and remember: you are important. Wishing you the very best — see you tomorrow!",
            onDismiss = { showSuccess = false }
        )
    }
}
