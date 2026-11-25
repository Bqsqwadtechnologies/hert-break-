package com.bqsqwad.heartbreakhealer.feature.entertainment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bqsqwad.heartbreakhealer.core.common.ads.AdsConfig
import com.bqsqwad.heartbreakhealer.core.ui.ads.BannerAdView
import com.bqsqwad.heartbreakhealer.core.ui.ads.NativeAdCard

@Composable
fun EntertainmentScreen(
    viewedStoryIds: Set<String>,
    seenJokeIds: Set<String>,
    onMarkStoryViewed: (String) -> Unit,
    onMarkJokeSeen: (String) -> Unit,
    activeStoryId: String?,
    activeStoryPart: Int,
    activeJokeIndex: Int,
    onSaveStoryProgress: (String, Int) -> Unit,
    onClearStoryProgress: () -> Unit,
    onSaveJokeProgress: (Int) -> Unit,
    onClearJokeProgress: () -> Unit,
    onUnlock: (String, () -> Unit) -> Unit,
    adsAllowed: Boolean,
    onBack: () -> Unit
) {
    var tab by remember { mutableStateOf("stories") }
    var activeStory by remember { mutableStateOf<StorySeries?>(null) }
    var partIndex by remember { mutableIntStateOf(0) }
    var isUnlocking by remember { mutableStateOf(false) }
    var joke by remember { mutableStateOf<Pair<Int, String>?>(null) }
    val unseenStories = EntertainmentCatalog.stories.filter { it.id !in viewedStoryIds }
    val unseenJokes = EntertainmentCatalog.jokes.indices.filter { "joke_${it + 1}" !in seenJokeIds }

    // Keep the unlock action locked until the rewarded-ad flow reports success/fallback.
    // A short UI timer here used to re-enable the button while an ad was still loading.
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("A Little Light", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Stories, smiles and a softer moment", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            TextButton(onClick = onBack) { Text("Back") }
        }
        TabRow(selectedTabIndex = if (tab == "stories") 0 else 1) {
            Tab(selected = tab == "stories", onClick = { tab = "stories"; activeStory = null; joke = null }, text = { Text("Stories") })
            Tab(selected = tab == "jokes", onClick = { tab = "jokes"; activeStory = null; joke = null }, text = { Text("Jokes") })
        }

        if (activeStory == null && joke == null) {
            if (tab == "stories") {
                LazyColumn(
                    Modifier.weight(1f).padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        Text("A story chosen just for this visit", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Mysteries, discoveries and uplifting endings that make you want to read one more part.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    item {
                        if (unseenStories.isNotEmpty()) {
                            Button(
                                onClick = {
                                    if (isUnlocking) return@Button
                                    isUnlocking = true
                                    val story = unseenStories.random()
                                    onUnlock("story_part_${Math.floorMod(story.id.hashCode(), 20) + 1}") {
                                        isUnlocking = false
                                        onMarkStoryViewed(story.id)
                                        onSaveStoryProgress(story.id, 0)
                                        activeStory = story
                                        partIndex = 0
                                    }
                                },
                                enabled = !isUnlocking,
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Surprise me with a story") }
                            // Story library count intentionally hidden.
                        } else {
                            Text("You've read every story in this collection. More stories will arrive in a future content update.", modifier = Modifier.padding(18.dp))
                        }
                        if (activeStoryId != null) {
                            val savedStory = EntertainmentCatalog.stories.firstOrNull { it.id == activeStoryId }
                            if (savedStory != null) {
                                OutlinedButton(
                                    onClick = {
                                        if (isUnlocking) return@OutlinedButton
                                        isUnlocking = true
                                        val targetPart = activeStoryPart.coerceIn(0, savedStory.parts.lastIndex)
                                        onUnlock("story_part_${Math.floorMod(savedStory.id.hashCode() + targetPart, 20) + 1}") {
                                            isUnlocking = false
                                            activeStory = savedStory
                                            partIndex = targetPart
                                        }
                                    },
                                    enabled = !isUnlocking,
                                    modifier = Modifier.fillMaxWidth()
                                ) { Text("Resume ${savedStory.title}") }
                            }
                        }
                    }
                    item { NativeAdCard(unitId = AdsConfig.NATIVE_LIBRARY, enabled = adsAllowed) }
                }
            } else {
                Column(Modifier.weight(1f).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text("A little laugh can brighten the moment.", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    if (activeJokeIndex in EntertainmentCatalog.jokes.indices) {
                        Spacer(Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = {
                                if (isUnlocking) return@OutlinedButton
                                isUnlocking = true
                                onUnlock("joke") {
                                    isUnlocking = false
                                    joke = activeJokeIndex to EntertainmentCatalog.jokes[activeJokeIndex]
                                }
                            },
                            enabled = !isUnlocking,
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Resume your last joke") }
                        Spacer(Modifier.height(18.dp))
                    }
                    Button(
                        onClick = {
                            if (isUnlocking) return@Button
                            if (unseenJokes.isNotEmpty()) {
                                isUnlocking = true
                                val selected = unseenJokes.random()
                                onUnlock("joke") {
                                    isUnlocking = false
                                    onMarkJokeSeen("joke_${selected + 1}")
                                    onSaveJokeProgress(selected)
                                    joke = selected to EntertainmentCatalog.jokes[selected]
                                }
                            }
                        },
                        enabled = unseenJokes.isNotEmpty() && !isUnlocking
                    ) { Text("Get a random joke") }
                    if (unseenJokes.isEmpty()) Text("You've seen every joke in this collection. New jokes will be added in a future update.", textAlign = TextAlign.Center)
                    Spacer(Modifier.height(12.dp))
                    Text("${seenJokeIds.size} jokes enjoyed", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            BannerAdView(unitId = AdsConfig.BANNER_LIBRARY, enabled = adsAllowed)
        }

        activeStory?.let { story ->
            val part = story.parts[partIndex]
            LazyColumn(
                Modifier.weight(1f).padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(story.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(part.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                }
                items(part.paragraphs.size) { index ->
                    Text(
                        part.paragraphs[index],
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.25f
                    )
                    if ((index + 1) % 2 == 0) {
                        NativeAdCard(unitId = AdsConfig.NATIVE_LIBRARY, enabled = adsAllowed)
                    }
                }
                item {
                    if (partIndex < story.parts.lastIndex) {
                        Button(
                            onClick = {
                                if (isUnlocking) return@Button
                                isUnlocking = true
                                onUnlock("story_part_${Math.floorMod(story.id.hashCode() + partIndex + 1, 20) + 1}") {
                                    isUnlocking = false
                                    partIndex++
                                    onSaveStoryProgress(story.id, partIndex)
                                }
                            },
                            enabled = !isUnlocking,
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Continue reading") }
                    } else {
                        Text("The End", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        Text("Please update Heartbreak Healer to enjoy the latest stories and improvements.", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)

                    }
                    OutlinedButton(
                        onClick = {
                            if (partIndex == story.parts.lastIndex) onClearStoryProgress()
                            activeStory = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Back to Stories") }
                }
            }
            // Keep an adaptive AdMob banner on every individual story part.
            BannerAdView(unitId = AdsConfig.BANNER_LIBRARY, enabled = adsAllowed)
        }

        joke?.let { selected ->
            Column(
                Modifier.weight(1f).fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("😂", style = MaterialTheme.typography.displayLarge)
                Spacer(Modifier.height(18.dp))
                Text(selected.second, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(26.dp))
                Button(onClick = {
                    if (isUnlocking) return@Button
                    val next = unseenJokes.firstOrNull { it != selected.first }
                    if (next == null) {
                        joke = null
                        onClearJokeProgress()
                    } else {
                        isUnlocking = true
                        onUnlock("joke") {
                            isUnlocking = false
                            onMarkJokeSeen("joke_${next + 1}")
                            onSaveJokeProgress(next)
                            joke = next to EntertainmentCatalog.jokes[next]
                        }
                    }
                }, modifier = Modifier.fillMaxWidth(), enabled = unseenJokes.isNotEmpty() && !isUnlocking) { Text("That's funny — next one") }
                OutlinedButton(onClick = { joke = null }, modifier = Modifier.fillMaxWidth()) { Text("Back to jokes") }
            }
            BannerAdView(unitId = AdsConfig.BANNER_LIBRARY, enabled = adsAllowed)
        }
    }
}
