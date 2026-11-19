package com.bqsqwad.heartbreakhealer.feature.journal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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

data class LetterUi(val id: Long, val content: String, val status: String)

private val BrandPrimary = Color(0xFFE51D3E)
private val BrandBlush = Color(0xFFFFF0F3)
private val BrandInk = Color(0xFF1E1419)
private val BrandMuted = Color(0xFF75626A)
private val BrandBorder = Color(0xFFFFD6DE)

@Composable
fun UnsentLettersScreen(
    letters: List<LetterUi> = emptyList(),
    onSave: (String) -> Unit = {},
    onRelease: (Long) -> Unit = {},
    adsAllowed: Boolean = false,
    onBack: () -> Unit = {}
) {
    var writing by remember { mutableStateOf(false) }
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
                    text = "Unsent Letters",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = BrandInk
                )
                Text(
                    text = "Speak without holding back · Symbolic release",
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

        // Middle Section: Letters list or Composer
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (writing) {
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
                            text = "Write whatever you never got to say",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandInk
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "There is no send button, no reply, and no consequences. Say it all.",
                            style = MaterialTheme.typography.bodySmall,
                            color = BrandMuted
                        )
                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            placeholder = {
                                Text(
                                    "Dear... I needed you to know how much it hurt, and what I wish had happened differently...",
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
                if (letters.isEmpty()) {
                    // Centered soothing card in the middle of the screen
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
                                Text("✉️", style = MaterialTheme.typography.headlineMedium)
                            }
                            Spacer(Modifier.height(14.dp))
                            Text(
                                text = "Speak Without Fear",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandInk,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "An unsent letter helps you close chapters you never got closure on. Write to your ex, to yourself, or to the future.",
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
                                text = "${letters.size} Letters Written",
                                style = MaterialTheme.typography.labelLarge,
                                color = BrandMuted,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                        items(letters, key = { it.id }) { letter ->
                            val isDraft = letter.status == "DRAFT"

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, if (isDraft) BrandBorder else Color(0xFFF0EDF5))
                            ) {
                                Column(Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isDraft) BrandBlush else Color(0xFFE8F5E9)
                                        ) {
                                            Text(
                                                text = if (isDraft) "✉️ Saved Draft" else "🕊️ Released & Let Go",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (isDraft) BrandPrimary else Color(0xFF2E7D32),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        text = letter.content,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = BrandInk
                                    )
                                    if (isDraft) {
                                        Spacer(Modifier.height(10.dp))
                                        Button(
                                            onClick = { onRelease(letter.id) },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C1820))
                                        ) {
                                            Text("🔥  Release & Burn (Let Go)", style = MaterialTheme.typography.labelMedium)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Down Section: Action buttons at the bottom, above banner ad
        if (writing) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { writing = false },
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
                            onSave(text)
                            text = ""
                            writing = false
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                ) {
                    Text("Save Draft", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        } else {
            Button(
                onClick = { writing = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
            ) {
                Text(
                    text = "✉️  Write an Unsent Letter",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Preserved Banner Ad in footer
        Spacer(Modifier.height(10.dp))
        BannerAdView(unitId = AdsConfig.BANNER_LIBRARY, enabled = adsAllowed)
    }
}
