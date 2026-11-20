package com.bqsqwad.heartbreakhealer.feature.dashboard

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bqsqwad.heartbreakhealer.core.common.ads.AdsConfig
import com.bqsqwad.heartbreakhealer.core.ui.ads.BannerAdView
import kotlinx.coroutines.delay

enum class BreathPattern(
    val title: String,
    val subtitle: String,
    val inhaleSeconds: Int,
    val holdSeconds: Int,
    val exhaleSeconds: Int,
    val restSeconds: Int,
    val isPremium: Boolean,
    val totalCycles: Int
) {
    VAGUS_478(
        title = "4-7-8 Vagus Reset",
        subtitle = "Inhale 4s · Hold 7s · Exhale 8s. Clinically proven to halt panic and anxiety.",
        inhaleSeconds = 4,
        holdSeconds = 7,
        exhaleSeconds = 8,
        restSeconds = 1,
        isPremium = false,
        totalCycles = 5
    ),
    BOX_BREATHING(
        title = "Box Breathing (4-4-4-4)",
        subtitle = "Inhale 4s · Hold 4s · Exhale 4s · Hold 4s. Used for immediate focus and calm.",
        inhaleSeconds = 4,
        holdSeconds = 4,
        exhaleSeconds = 4,
        restSeconds = 4,
        isPremium = false,
        totalCycles = 6
    ),
    PHYSIOLOGICAL_SIGH(
        title = "Physiological Sigh",
        subtitle = "Double inhale · Extended slow exhale. Fastest biological heart rate decelerator.",
        inhaleSeconds = 3,
        holdSeconds = 1,
        exhaleSeconds = 6,
        restSeconds = 1,
        isPremium = false,
        totalCycles = 8
    ),
    DEEP_SOMATIC_10(
        title = "10-Min Extended Somatic Reset",
        subtitle = "Full guided nervous system reset with somatic muscle release cues.",
        inhaleSeconds = 4,
        holdSeconds = 7,
        exhaleSeconds = 8,
        restSeconds = 2,
        isPremium = true,
        totalCycles = 20
    )
}

enum class BreathPhase(val instruction: String, val cue: String) {
    INHALE("Breathe In Slowly", "Inhale gently through your nose"),
    HOLD("Gently Hold", "Keep your chest still, soften your shoulders"),
    EXHALE("Release & Exhale", "Slow, steady exhale through your mouth"),
    REST("Rest", "Let your body settle in quiet stillness")
}

@Composable
fun SomaticBreathworkScreen(
    unlockedDeepSession: Boolean = false,
    onUnlockDeepSession: () -> Unit = {},
    onWatchHouseAd: () -> Unit = {},
    adsAllowed: Boolean = true,
    onBack: () -> Unit = {}
) {
    val ink = Color(0xFF17172A)
    val muted = Color(0xFF666477)
    val blush = Color(0xFFFFF0F3)
    val primaryRed = Color(0xFFE51D3E)

    var selectedPattern by remember { mutableStateOf(BreathPattern.VAGUS_478) }
    var isActive by remember { mutableStateOf(false) }
    var currentPhase by remember { mutableStateOf(BreathPhase.INHALE) }
    var secondsRemainingInPhase by remember { mutableIntStateOf(BreathPattern.VAGUS_478.inhaleSeconds) }
    var currentCycle by remember { mutableIntStateOf(1) }
    var showUnlockDialog by remember { mutableStateOf(false) }

    val somaticGroundingCues = remember {
        listOf(
            "Unclench your jaw and let your teeth part slightly.",
            "Drop your shoulders away from your ears.",
            "Softly loosen your belly muscles; let your breath expand your ribs.",
            "Feel the solid ground beneath you supporting your weight.",
            "Notice that right here, right now, you are safe.",
            "You do not need to figure out everything today. Just breathe."
        )
    }
    var currentSomaticCue by remember { mutableStateOf(somaticGroundingCues[0]) }

    LaunchedEffect(currentCycle) {
        currentSomaticCue = somaticGroundingCues[(currentCycle - 1) % somaticGroundingCues.size]
    }

    // Breathing Engine Timer Loop
    LaunchedEffect(isActive, selectedPattern) {
        if (!isActive) return@LaunchedEffect

        currentPhase = BreathPhase.INHALE
        secondsRemainingInPhase = selectedPattern.inhaleSeconds
        currentCycle = 1

        while (isActive && currentCycle <= selectedPattern.totalCycles) {
            // INHALE
            currentPhase = BreathPhase.INHALE
            for (s in selectedPattern.inhaleSeconds downTo 1) {
                if (!isActive) break
                secondsRemainingInPhase = s
                delay(1000)
            }
            if (!isActive) break

            // HOLD
            if (selectedPattern.holdSeconds > 0) {
                currentPhase = BreathPhase.HOLD
                for (s in selectedPattern.holdSeconds downTo 1) {
                    if (!isActive) break
                    secondsRemainingInPhase = s
                    delay(1000)
                }
            }
            if (!isActive) break

            // EXHALE
            currentPhase = BreathPhase.EXHALE
            for (s in selectedPattern.exhaleSeconds downTo 1) {
                if (!isActive) break
                secondsRemainingInPhase = s
                delay(1000)
            }
            if (!isActive) break

            // REST
            if (selectedPattern.restSeconds > 0) {
                currentPhase = BreathPhase.REST
                for (s in selectedPattern.restSeconds downTo 1) {
                    if (!isActive) break
                    secondsRemainingInPhase = s
                    delay(1000)
                }
            }
            if (!isActive) break

            currentCycle++
        }

        if (currentCycle > selectedPattern.totalCycles) {
            isActive = false
        }
    }

    val targetScale = when (currentPhase) {
        BreathPhase.INHALE -> 1.35f
        BreathPhase.HOLD -> 1.35f
        BreathPhase.EXHALE -> 0.90f
        BreathPhase.REST -> 0.90f
    }
    val animatedScale by animateFloatAsState(
        targetValue = if (isActive) targetScale else 1.0f,
        animationSpec = tween(
            durationMillis = when (currentPhase) {
                BreathPhase.INHALE -> selectedPattern.inhaleSeconds * 1000
                BreathPhase.HOLD -> 400
                BreathPhase.EXHALE -> selectedPattern.exhaleSeconds * 1000
                BreathPhase.REST -> 400
            },
            easing = FastOutSlowInEasing
        ),
        label = "breath_scale"
    )

    // Unlock Dialog for Deep Session
    if (showUnlockDialog) {
        Dialog(onDismissRequest = { showUnlockDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column(Modifier.padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🫁", fontSize = 42.sp)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Unlock 10-Min Somatic Reset",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ink,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "An extended restorative breathing program to deeply calm high-stress panic spikes, regulate cortisol, and rebuild nervous system safety.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = muted,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(14.dp))
                    Text(
                        "Watch a quick rewarded video or partner spotlight to unlock this premium deep somatic session.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ink,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(18.dp))
                    Button(
                        onClick = {
                            showUnlockDialog = false
                            onUnlockDeepSession()
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryRed)
                    ) {
                        Text("▶  Watch Video to Unlock", fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            showUnlockDialog = false
                            onWatchHouseAd()
                        },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("⭐  BQ Sqwad AI Partner Spotlight")
                    }
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = { showUnlockDialog = false }) {
                        Text("Maybe later", color = muted)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFD))
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Somatic Breathwork",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ink
                )
                Text(
                    "Regulate your nervous system in minutes",
                    style = MaterialTheme.typography.bodySmall,
                    color = muted
                )
            }
            TextButton(onClick = onBack) {
                Text("Done", fontWeight = FontWeight.SemiBold, color = primaryRed)
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Instant Panic SOS Button
            item {
                Card(
                    onClick = {
                        selectedPattern = BreathPattern.PHYSIOLOGICAL_SIGH
                        isActive = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFECEF)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, primaryRed.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⚡", fontSize = 24.sp)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                "Panic Reset SOS",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = primaryRed
                            )
                            Text(
                                "Racing heart or panic spike? Tap for instant Physiological Sigh reset.",
                                style = MaterialTheme.typography.bodySmall,
                                color = ink
                            )
                        }
                    }
                }
            }

            // Animated Breathing Orb Box
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            selectedPattern.title,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = primaryRed
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            if (isActive) "Cycle $currentCycle of ${selectedPattern.totalCycles}" else "Tap Start to begin guided breathing",
                            style = MaterialTheme.typography.labelSmall,
                            color = muted
                        )
                        Spacer(Modifier.height(28.dp))

                        // Pulsing Visualizer Circle
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(200.dp)
                        ) {
                            // Outer soft glow
                            Box(
                                modifier = Modifier
                                    .size(190.dp)
                                    .scale(animatedScale)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                primaryRed.copy(alpha = 0.25f),
                                                blush.copy(alpha = 0.1f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )
                            // Inner breathing core
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(130.dp)
                                    .scale(animatedScale)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color(0xFFFF5277), primaryRed)
                                        )
                                    )
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        if (isActive) "$secondsRemainingInPhase" else "Ready",
                                        color = Color.White,
                                        fontSize = 36.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (isActive) {
                                        Text(
                                            "seconds",
                                            color = Color.White.copy(alpha = 0.8f),
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(24.dp))
                        Text(
                            if (isActive) currentPhase.instruction else "Take a comfortable posture",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = ink,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            if (isActive) currentPhase.cue else "Breathe naturally before beginning",
                            style = MaterialTheme.typography.bodyMedium,
                            color = muted,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(20.dp))

                        // Controls
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = { isActive = !isActive },
                                modifier = Modifier.height(48.dp).weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isActive) Color(0xFF33334D) else primaryRed
                                )
                            ) {
                                Text(
                                    if (isActive) "❚❚  Pause" else "▶  Start Breathing",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            if (isActive || currentCycle > 1) {
                                OutlinedButton(
                                    onClick = {
                                        isActive = false
                                        currentCycle = 1
                                        secondsRemainingInPhase = selectedPattern.inhaleSeconds
                                    },
                                    modifier = Modifier.height(48.dp),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text("Reset")
                                }
                            }
                        }
                    }
                }
            }

            // Somatic Grounding Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = blush)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🌿", fontSize = 20.sp)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Somatic Body Cue",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ink
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            currentSomaticCue,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF2E2C3D)
                        )
                    }
                }
            }

            // Technique Selector List
            item {
                Text(
                    "Choose Breathing Protocol",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ink,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }

            items(BreathPattern.values().size) { index ->
                val pattern = BreathPattern.values()[index]
                val isSelected = selectedPattern == pattern
                val isUnlocked = !pattern.isPremium || unlockedDeepSession

                Card(
                    onClick = {
                        if (pattern.isPremium && !isUnlocked) {
                            showUnlockDialog = true
                        } else {
                            selectedPattern = pattern
                            isActive = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFF0F4FF) else Color.White
                    ),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, primaryRed) else null
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    pattern.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ink
                                )
                                if (pattern.isPremium && !isUnlocked) {
                                    Spacer(Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFFFECC8)
                                    ) {
                                        Text(
                                            "PREMIUM",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF945200)
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                pattern.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = muted
                            )
                        }
                        if (pattern.isPremium && !isUnlocked) {
                            Text("🔒", fontSize = 18.sp)
                        } else if (isSelected) {
                            Text("✓", color = primaryRed, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                    }
                }
            }

            // BQ Sqwad AI Partner Spotlight Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EEFF))
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🤖", fontSize = 24.sp)
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Want live AI support while breathing?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF17172A)
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "BQ Sqwad AI Companion has real-time voice chat and compassionate presence. Watch the video to see how it works.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF3D3B52)
                        )
                        Spacer(Modifier.height(10.dp))
                        Button(
                            onClick = onWatchHouseAd,
                            modifier = Modifier.fillMaxWidth().height(42.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Watch BQ Sqwad AI Presentation")
                        }
                    }
                }
            }

            // Banner Ad Placement
            item {
                Spacer(Modifier.height(8.dp))
                BannerAdView(unitId = AdsConfig.BANNER_LIBRARY, enabled = adsAllowed)
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
