package com.bqsqwad.heartbreakhealer.feature.heartjourney

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bqsqwad.heartbreakhealer.core.domain.journey.JourneyState
import com.bqsqwad.heartbreakhealer.core.common.ads.AdsConfig
import com.bqsqwad.heartbreakhealer.core.ui.ads.BannerAdView
import java.time.LocalDate

/**
 * [onOpenFeature] must be the SAME path the Home tiles use (it spends 1 Heart for optional tools). `free` = true for SOS.
 * [nextStepRoute] is the route of the 30-Day Rebuild, or the 14-Day Healing Path once Rebuild is finished.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeartJourneyScreen(
    reduceMotion: Boolean,
    nextStepRoute: String,
    onOpenFeature: (route: String, free: Boolean) -> Unit,
    onBack: () -> Unit,
    adsAllowed: Boolean = false,
    onPromoVideoFinished: (() -> Unit)? = null,
    vm: HeartJourneyViewModel = hiltViewModel(),
) {
    val s: JourneyState = vm.state.collectAsState().value ?: return
    val context = LocalContext.current
    var showCompanionVideo by remember { mutableStateOf(false) }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Heart Rebuild", fontWeight = FontWeight.Bold) }, navigationIcon = { TextButton(onClick = onBack) { Text("‹ Back") } }) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { pad ->
        LazyColumn(Modifier.fillMaxSize().padding(pad), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                HealingHeartCard(state = s, reduceMotion = reduceMotion, celebrating = s.celebrating(LocalDate.now().toEpochDay()), onOpen = {}, onBrokenAgain = { vm.startRound() })
            }
            item {
                Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0F3)), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("For today", fontWeight = FontWeight.Bold, color = Color(0xFF17172A))
                        Text(JourneyMessages.dailyFor(s.completedDays), color = Color(0xFF3D3B52), modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
            item { Text("Today's 3 steps", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            item { RequiredRow("📖", "Read 1 story", if (s.today.story) "Done" else "Not yet", s.today.story) { onOpenFeature("entertainment", false) } }
            item { RequiredRow("😄", "Enjoy 2 jokes", "${s.today.jokes.coerceAtMost(2)} of 2", s.today.jokesDone) { onOpenFeature("entertainment", false) } }
            item { RequiredRow("🌱", "Complete 1 step of your 30-Day Rebuild or 14-Day Path", if (s.today.rebuildStep) "Done" else "Not yet", s.today.rebuildStep) { onOpenFeature(nextStepRoute, false) } }
            item { Text("Everything in the app (checks itself)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            items(JourneyItems.all, key = { it.id }) { item ->
                val done = item.detect.any { it in s.doneToday }
                Card(
                    onClick = { item.route?.let { onOpenFeature(it, item.free) } },
                    shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = if (done) Color(0xFFE5F5EB) else Color.White),
                    elevation = CardDefaults.cardElevation(1.dp), modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(item.emoji, style = MaterialTheme.typography.titleLarge)
                        Text(item.title, Modifier.weight(1f).padding(start = 12.dp), color = Color(0xFF17172A))
                        Text(if (done) "✓ Done today" else "›", color = if (done) Color(0xFF218B52) else MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            item {
                CompanionCard(
                    onProceed = { CompanionLinks.open(context) },
                    onNoThanks = { showCompanionVideo = true },
                    onUsingAlready = { showCompanionVideo = true },
                )
            }
            item {
                Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Daily reminder", Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                            Switch(checked = s.reminderEnabled, onCheckedChange = { vm.setReminder(it, s.reminderHour) })
                        }
                        Text("Time of day", style = MaterialTheme.typography.labelMedium, color = Color(0xFF666477), modifier = Modifier.padding(top = 6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(8, 12, 17, 19, 21).forEach { hour ->
                                FilterChip(selected = s.reminderHour == hour, onClick = { vm.setReminder(s.reminderEnabled, hour) }, label = { Text("%d:00".format(hour)) })
                            }
                        }
                    }
                }
            }
            if (s.finished) item {
                Button(onClick = { vm.startRound() }, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(16.dp)) { Text("Broken again") }
            }
            item { BannerAdView(unitId = AdsConfig.BANNER_LIST, enabled = adsAllowed) }
        }
    }
    if (showCompanionVideo) HouseVideoAd(onClose = { showCompanionVideo = false; onPromoVideoFinished?.invoke() })
}

@Composable
private fun RequiredRow(emoji: String, title: String, status: String, done: Boolean, onGo: () -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = if (done) Color(0xFFE5F5EB) else Color.White),
        elevation = CardDefaults.cardElevation(2.dp), modifier = Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, style = MaterialTheme.typography.headlineSmall)
            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(title, fontWeight = FontWeight.SemiBold, color = Color(0xFF17172A))
                Text(status, style = MaterialTheme.typography.bodySmall, color = if (done) Color(0xFF218B52) else Color(0xFF666477))
            }
            if (!done) Button(onClick = onGo, shape = RoundedCornerShape(12.dp)) { Text("Go") } else Text("✓", color = Color(0xFF218B52), style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        }
    }
}
