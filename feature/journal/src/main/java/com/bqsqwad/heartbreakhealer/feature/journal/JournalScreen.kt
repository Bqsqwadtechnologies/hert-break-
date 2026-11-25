package com.bqsqwad.heartbreakhealer.feature.journal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bqsqwad.heartbreakhealer.core.common.ads.AdsConfig
import com.bqsqwad.heartbreakhealer.core.ui.ads.BannerAdView

data class JournalUiEntry(val id: Long, val date: String, val mood: Int, val content: String)

private val BrandPrimary = Color(0xFFE51D3E)
private val BrandBlush = Color(0xFFFFF0F3)
private val BrandInk = Color(0xFF1E1419)
private val BrandMuted = Color(0xFF75626A)
private val BrandBorder = Color(0xFFFFD6DE)

private val MoodOptions = listOf(
    1 to ("💔" to "Heavy"),
    2 to ("🌧️" to "Hurting"),
    3 to ("⛅" to "Neutral"),
    4 to ("🌤️" to "Calmer"),
    5 to ("💖" to "Strong")
)

@Composable
fun JournalScreen(
    entries: List<JournalUiEntry> = emptyList(),
    onSaveEntry: (mood: Int, content: String) -> Unit = { _, _ -> },
    adsAllowed: Boolean = false,
    onBack: () -> Unit = {}
) {
    var showEditor by remember { mutableStateOf(false) }
    var mood by remember { mutableIntStateOf(3) }
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFD))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Healing Journal",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = BrandInk
                )
                Text(
                    text = "100% private · Stored securely on your device",
                    style = MaterialTheme.typography.bodySmall,
                    color = BrandMuted
                )
            }
            TextButton(
                onClick = onBack,
                colors = ButtonDefaults.textButtonColors(contentColor = BrandPrimary)
            ) {
                Text("Back", fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.height(14.dp))

        // Middle Section: Content or Editor
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (showEditor) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BrandBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "How does your heart feel today?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandInk
                        )
                        Spacer(Modifier.height(10.dp))

                        // Mood selection chips in the app's colors
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MoodOptions.forEach { (value, pair) ->
                                val (emoji, label) = pair
                                val isSelected = mood == value
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) BrandBlush else Color(0xFFF7F7FA))
                                        .clickable { mood = value }
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                ) {
                                    Text(text = emoji, style = MaterialTheme.typography.titleLarge)
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) BrandPrimary else BrandMuted
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            placeholder = {
                                Text(
                                    "Pour out whatever you're feeling... anger, sadness, relief, or gratitude. Everything is safe here.",
                                    color = BrandMuted.copy(alpha = 0.7f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPrimary,
                                unfocusedBorderColor = BrandBorder,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                    }
                }
            } else {
                if (entries.isEmpty()) {
                    // Centered soothing empty state in the middle of the screen
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .padding(horizontal = 8.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = BrandBlush),
                        border = BorderStroke(1.dp, BrandBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🕊️", style = MaterialTheme.typography.headlineMedium)
                            }
                            Spacer(Modifier.height(14.dp))
                            Text(
                                text = "A Safe Space for Your Heart",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandInk,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Healing is not a straight line. Write whenever emotions feel heavy. No one sees your entries except you.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = BrandMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                text = "${entries.size} Healing Journal Entries",
                                style = MaterialTheme.typography.labelLarge,
                                color = BrandMuted,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                        items(entries, key = { it.id }) { entry ->
                            val moodInfo = MoodOptions.firstOrNull { it.first == entry.mood }?.second
                            val moodEmoji = moodInfo?.first ?: "💭"
                            val moodLabel = moodInfo?.second ?: "Reflecting"

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, Color(0xFFF0EDF5))
                            ) {
                                Column(Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = BrandBlush
                                        ) {
                                            Text(
                                                text = "$moodEmoji $moodLabel",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = BrandPrimary,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                        Text(
                                            text = entry.date,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = BrandMuted
                                        )
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        text = entry.content,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = BrandInk
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Down Section: Action Buttons at the bottom, above banner ad
        if (showEditor) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { showEditor = false },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, BrandBorder)
                ) {
                    Text("Cancel", color = BrandMuted, fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = {
                        if (text.isNotBlank()) {
                            onSaveEntry(mood, text)
                            text = ""
                            showEditor = false
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                ) {
                    Text("Save Entry", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        } else {
            Button(
                onClick = { showEditor = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
            ) {
                Text(
                    text = "✍️  Write New Journal Entry",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Keep third-party advertising completely outside the private journal editor.
        if (!showEditor) {
            Spacer(Modifier.height(10.dp))
            BannerAdView(unitId = AdsConfig.BANNER_LIBRARY, enabled = adsAllowed)
        }
    }
}
