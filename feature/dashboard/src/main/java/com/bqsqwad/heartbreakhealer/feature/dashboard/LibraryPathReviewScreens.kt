package com.bqsqwad.heartbreakhealer.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bqsqwad.heartbreakhealer.core.common.ads.AdsConfig
import com.bqsqwad.heartbreakhealer.core.ui.ads.BannerAdView
import com.bqsqwad.heartbreakhealer.core.ui.ads.NativeAdCard
import com.bqsqwad.heartbreakhealer.core.ui.components.JourneyRewardOfferDialog
import com.bqsqwad.heartbreakhealer.core.ui.components.JourneySuccessDialog

data class LibraryArticle(val id: String, val title: String, val body: String)

private val libraryArticles = listOf(
    LibraryArticle("sleep", "Protect your sleep", "Healing is harder when you're exhausted. Aim for a regular wind-down: dim lights, phone away, same bedtime when you can."),
    LibraryArticle("friday", "Hard evenings & weekends", "Empty time used to be filled with them. Plan one small thing: a walk, a show, a call with someone safe."),
    LibraryArticle("nc", "Why no-contact helps", "Each urge you don't act on teaches your brain a new pattern. It's not punishment — it's recovery."),
    LibraryArticle("friends", "Telling friends", "You don't owe everyone the full story. \"I'm going through a breakup and need gentle company\" is enough."),
    LibraryArticle("social", "Social media", "Mute or unfollow if their presence keeps the wound open. You can always unmute later."),
    LibraryArticle("anger", "When anger shows up", "Anger can mean your boundaries matter. Write it, move your body, don't send it to them."),
    LibraryArticle("lonely", "Lonely vs missing them", "Loneliness wants connection. Missing them wants a specific person. Feed loneliness with safe people, not the old door."),
    LibraryArticle("relapse", "If you slip", "A text doesn't erase your progress. Notice it, restart no-contact, be kinder than your inner critic.")
)

@Composable
fun HealingLibraryScreen(onBack: () -> Unit, adsAllowed: Boolean = false) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text("Healing library", style = MaterialTheme.typography.headlineMedium)
                Text("Short reads. No lectures.", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(8.dp))
            }
            itemsIndexed(libraryArticles) { index, article ->
                if (index > 0 && index % 6 == 0) {
                    NativeAdCard(unitId = AdsConfig.NATIVE_LIBRARY, enabled = adsAllowed)
                }
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(article.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(article.body, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            item { TextButton(onClick = onBack) { Text("Back") } }
        }
        BannerAdView(unitId = AdsConfig.BANNER_LIBRARY, enabled = adsAllowed)
    }
}

data class PathDay(val day: Int, val title: String, val done: Boolean)

@Composable
fun HealingPathScreen(
    days: List<PathDay> = (1..14).map { PathDay(it, "Day $it — one small act of care", false) },
    completedToday: Boolean = false,
    onCompleteDay: (Int, (Boolean) -> Unit) -> Unit = { _, result -> result(false) },
    onWatchRewarded: ((() -> Unit) -> Unit) = { finished -> finished() },
    onBack: () -> Unit,
    adsAllowed: Boolean = false
) {
    var completedLocally by remember { mutableStateOf(emptySet<Int>()) }
    var blockedToday by remember(completedToday) { mutableStateOf(completedToday) }
    var showSuccess by remember { mutableStateOf(false) }
    val completed = days.filter { it.done }.map { it.day }.toSet() + completedLocally
    val nextDay = (1..14).firstOrNull { it !in completed } ?: 15

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text("14-Day Healing Path", style = MaterialTheme.typography.headlineMedium)
                Text("One gentle focus each day. Complete one step, then give yourself time to practise it.", style = MaterialTheme.typography.bodySmall)
                if (blockedToday && nextDay <= 14) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "You've completed your one journey task for today. Your next task unlocks tomorrow.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.height(8.dp))
            }
            itemsIndexed(days) { index, d ->
                if (index > 0 && index % 6 == 0) {
                    NativeAdCard(unitId = AdsConfig.NATIVE_PATHS, enabled = adsAllowed)
                }
                val isDone = d.done || d.day in completedLocally
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Day ${d.day}", style = MaterialTheme.typography.titleMedium)
                            Text(d.title, style = MaterialTheme.typography.bodySmall)
                        }
                        when {
                            isDone -> Text("Completed ✓", color = MaterialTheme.colorScheme.primary)
                            d.day != nextDay -> Text("Locked", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            blockedToday -> Text("Tomorrow", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            else -> Button(onClick = {
                                onCompleteDay(d.day) { success ->
                                    if (success) {
                                        completedLocally = completedLocally + d.day
                                        blockedToday = true
                                        onWatchRewarded { showSuccess = true }
                                    } else {
                                        blockedToday = true
                                    }
                                }
                            }) { Text("Complete") }
                        }
                    }
                }
            }
            item { TextButton(onClick = onBack) { Text("Back") } }
        }
        BannerAdView(unitId = AdsConfig.BANNER_PATHS, enabled = adsAllowed)
    }

    if (showSuccess) {
        JourneySuccessDialog(
            title = "Your healing matters",
            message = "You made time for yourself today. Healing is not a race, and you do not have to do everything at once. Keep choosing gentle steps, trust your progress, and remember that you matter. Wishing you brighter days ahead — see you tomorrow!",
            onDismiss = { showSuccess = false }
        )
    }
}

@Composable
fun WeeklyReviewScreen(
    checkIns: Int = 0,
    urgesRodeOut: Int = 0,
    noContactDays: Int = 0,
    onBack: () -> Unit,
    adsAllowed: Boolean = false
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("This week", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Check-ins: $checkIns", style = MaterialTheme.typography.bodyLarge)
                Text("Urges you rode out: $urgesRodeOut", style = MaterialTheme.typography.bodyLarge)
                Text("No-contact days: $noContactDays", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Showing up counts. You don't have to feel strong to be healing.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (adsAllowed) {
            Spacer(Modifier.height(16.dp))
            NativeAdCard(unitId = AdsConfig.NATIVE_WEEKLY_REVIEW, enabled = true)
        }
        Spacer(Modifier.height(24.dp))
        TextButton(onClick = onBack) { Text("Finish review") }
        Spacer(Modifier.weight(1f))
        // Weekly Review uses its dedicated native unit above; no duplicate banner here.
    }
}
