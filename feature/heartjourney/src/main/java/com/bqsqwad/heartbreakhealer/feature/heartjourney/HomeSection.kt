package com.bqsqwad.heartbreakhealer.feature.heartjourney

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.bqsqwad.heartbreakhealer.core.domain.journey.JourneyState
import java.time.LocalDate

/**
 * Drop-in slot for the top of Home. Shows, in this order of need:
 *  1. the one-time question (only if it was never answered),
 *  2. a quiet invitation (if the user answered "just checking"),
 *  3. the heart card (if the journey is running or finished).
 * It also owns the welcome, the "day counted" encouragement, the celebration and "Broken again".
 */
@Composable
fun HeartJourneyHomeSection(
    reduceMotion: Boolean,
    onOpenJourney: () -> Unit,
    onPromoVideoFinished: (() -> Unit)? = null,
    vm: HeartJourneyViewModel = hiltViewModel(),
) {
    val s: JourneyState = vm.state.collectAsState().value ?: return
    val today = LocalDate.now().toEpochDay()
    var showWelcome by remember { mutableStateOf(false) }
    var showBrokenAgain by remember { mutableStateOf(false) }
    var showCompanion by remember { mutableStateOf(false) }

    when {
        !s.asked -> BrokenPromptDialog(
            reduceMotion = reduceMotion,
            onYes = { vm.answerBroken(true); showWelcome = true },
            onNo = { vm.answerBroken(false) },
        )
        !s.joined -> InvitationCard(onStart = { vm.startRound(); showWelcome = true })
        else -> HealingHeartCard(
            state = s, reduceMotion = reduceMotion, celebrating = s.celebrating(today),
            onOpen = onOpenJourney, onBrokenAgain = { showBrokenAgain = true },
        )
    }

    if (showWelcome) JourneyWelcome(vm = vm, onBegin = { showWelcome = false })

    if (s.dayCountedPending && !s.finished) {
        val dueForCompanion = s.completedDays in setOf(3, 10, 20) && s.companionLastShownEpochDay < today - 6
        DayCountedDialog(
            day = s.completedDays, reduceMotion = reduceMotion,
            onDone = { vm.ackDayCounted(); if (dueForCompanion) { vm.companionShown(); showCompanion = true } },
        )
    }
    if (showCompanion) CompanionPromptDialog(onClose = { showCompanion = false }, onPromoVideoFinished = onPromoVideoFinished)

    if (s.celebrationPending) CelebrationDialog(reduceMotion = reduceMotion, onDone = { vm.ackCelebration() })

    if (showBrokenAgain) BrokenAgainDialog(
        reduceMotion = reduceMotion,
        onStart = { showBrokenAgain = false; vm.startRound(); showWelcome = true },
        onNotNow = { showBrokenAgain = false },
    )
}

@Composable
private fun InvitationCard(onStart: () -> Unit) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0F3)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Ready to rebuild your heart?", fontWeight = FontWeight.Bold, color = Color(0xFF17172A))
                Text("A gentle 30-day journey. Start whenever you like.", style = MaterialTheme.typography.bodySmall, color = Color(0xFF666477))
            }
            Button(onClick = onStart, shape = RoundedCornerShape(14.dp)) { Text("Start") }
        }
    }
}

@Composable
fun BrokenPromptDialog(reduceMotion: Boolean, onYes: () -> Unit, onNo: () -> Unit) {
    Dialog(onDismissRequest = {}, properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)) {
        Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 8.dp) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                HealingHeart(0f, Modifier.size(120.dp), reduceMotion)
                Spacer(Modifier.height(12.dp))
                Text("Are you heartbroken right now?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Be honest. There is no wrong answer, and we will only ask you this once.",
                    textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(20.dp))
                Button(onClick = onYes, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp)) { Text("Yes, I am") }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = onNo, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(16.dp)) {
                    Text("I just came to check something")
                }
            }
        }
    }
}

/** Shown right after "Yes, I am": encouragement, "I'm with you", the rules of a day, then every feature in the app. */
@Composable
fun JourneyWelcome(vm: HeartJourneyViewModel, onBegin: () -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { onBegin() }
    Dialog(onDismissRequest = {}, properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = false, dismissOnClickOutside = false)) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            LazyColumn(Modifier.padding(horizontal = 22.dp), horizontalAlignment = Alignment.CenterHorizontally, contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 28.dp)) {
                item {
                    HealingHeart(0.08f, Modifier.size(150.dp))
                    Spacer(Modifier.height(10.dp))
                    Text("Choosing to heal takes courage.", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(6.dp))
                    Text("I'm with you. Let's rebuild together.", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(14.dp))
                    Text(
                        "Your 30-day heart rebuild starts now. Each day, read 1 story, enjoy 2 jokes, and complete 1 step of your 30-Day Rebuild or 14-Day Healing Path. Your heart closes a little more every day.",
                        textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(10.dp))
                    Text("Everything below checks itself off as you use it:", fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                }
                items(JourneyItems.all, key = { it.id }) { item ->
                    Surface(Modifier.fillMaxWidth().padding(vertical = 3.dp), shape = RoundedCornerShape(14.dp), color = Color(0xFFFFF0F3)) {
                        Text("${item.emoji}  ${item.title}", Modifier.padding(14.dp), color = Color(0xFF17172A))
                    }
                }
                item {
                    Spacer(Modifier.height(18.dp))
                    Button(
                        onClick = {
                            vm.setReminder(true, 19)
                            val needs = Build.VERSION.SDK_INT >= 33 &&
                                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
                            if (needs) launcher.launch(Manifest.permission.POST_NOTIFICATIONS) else onBegin()
                        },
                        modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp),
                    ) { Text("Begin my 30 days") }
                    Text("We will remind you once a day. You can change this any time.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp))
                }
            }
        }
    }
}

@Composable
fun DayCountedDialog(day: Int, reduceMotion: Boolean, onDone: () -> Unit) {
    Dialog(onDismissRequest = onDone) {
        Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 10.dp) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                HealingHeart(day / 30f, Modifier.size(110.dp), reduceMotion)
                Spacer(Modifier.height(10.dp))
                Text("Day $day counted ✓", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                Text(JourneyMessages.afterTaskFor(LocalDate.now().toEpochDay()), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(18.dp))
                Button(onClick = onDone, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(16.dp)) { Text("See you tomorrow") }
            }
        }
    }
}

@Composable
fun CelebrationDialog(reduceMotion: Boolean, onDone: () -> Unit) {
    Dialog(onDismissRequest = onDone) {
        Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 10.dp) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                HealingHeart(1f, Modifier.size(140.dp), reduceMotion)
                Spacer(Modifier.height(10.dp))
                Text("Your heart is healed 🌸", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                Text(JourneyMessages.celebration[0], textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(6.dp))
                Text("Keep checking in regularly. We are still here for you.", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(18.dp))
                Button(onClick = onDone, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(16.dp)) { Text("Celebrate") }
            }
        }
    }
}

@Composable
fun BrokenAgainDialog(reduceMotion: Boolean, onStart: () -> Unit, onNotNow: () -> Unit) {
    Dialog(onDismissRequest = onNotNow) {
        Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 10.dp) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                HealingHeart(0.02f, Modifier.size(110.dp), reduceMotion)
                Spacer(Modifier.height(10.dp))
                Text("It's okay. We start again, together.", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                Text(JourneyMessages.brokenAgainFor(LocalDate.now().toEpochDay()), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(18.dp))
                Button(onClick = onStart, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(16.dp)) { Text("Start my 30 days again") }
                TextButton(onClick = onNotNow) { Text("Not now") }
            }
        }
    }
}
