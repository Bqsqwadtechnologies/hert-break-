package com.bqsqwad.heartbreakhealer.feature.sos

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SosScreen(
    onNavigateToSupport: () -> Unit,
    onClose: () -> Unit
) {
    var mode by remember { mutableStateOf<SosMode>(SosMode.Menu) }

    when (val current = mode) {
        SosMode.Menu -> SosMenu(
            onBreathe = { mode = SosMode.Breathe },
            onDistraction = { mode = SosMode.Distraction },
            onGrounding = { mode = SosMode.Grounding },
            onReasons = { mode = SosMode.Reasons },
            onDontText = { mode = SosMode.DontText },
            onSupport = onNavigateToSupport,
            onClose = onClose
        )
        SosMode.Breathe -> BreathingExercise(onDone = { mode = SosMode.Menu })
        SosMode.Distraction -> DistractionTimer(onDone = { mode = SosMode.Menu })
        SosMode.Grounding -> GroundingExercise(onDone = { mode = SosMode.Menu })
        SosMode.Reasons -> ReasonsList(onDone = { mode = SosMode.Menu })
        SosMode.DontText -> DontTextCountdown(onDone = { mode = SosMode.Menu })
    }
}

private sealed class SosMode {
    data object Menu : SosMode()
    data object Breathe : SosMode()
    data object Distraction : SosMode()
    data object Grounding : SosMode()
    data object Reasons : SosMode()
    data object DontText : SosMode()
}

@Composable
private fun SosMenu(
    onBreathe: () -> Unit,
    onDistraction: () -> Unit,
    onGrounding: () -> Unit,
    onReasons: () -> Unit,
    onDontText: () -> Unit,
    onSupport: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Urge SOS",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "You're stronger than this moment. Pick something that helps.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))

        SosCard("Breathe with me", "Guided breathing animation") { onBreathe() }
        SosCard("5-minute distraction", "Timer to ride out the urge") { onDistraction() }
        SosCard("Grounding exercise", "5-4-3-2-1 senses") { onGrounding() }
        SosCard("Reasons I'm better off", "Your personal list") { onReasons() }
        SosCard("Don't text them", "Delay countdown") { onDontText() }

        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = onSupport, modifier = Modifier.fillMaxWidth()) {
            Text("Need more support?")
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onClose, modifier = Modifier.fillMaxWidth()) {
            Text("I'm okay for now")
        }
    }
}

@Composable
private fun SosCard(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun BreathingExercise(onDone: () -> Unit) {
    val scale = remember { Animatable(0.6f) }
    var phase by remember { mutableStateOf("Inhale") }

    LaunchedEffect(Unit) {
        while (true) {
            phase = "Inhale"
            scale.animateTo(1.0f, tween(4000, easing = LinearEasing))
            phase = "Hold"
            delay(2000)
            phase = "Exhale"
            scale.animateTo(0.6f, tween(4000, easing = LinearEasing))
            delay(1000)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(phase, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
            Canvas(modifier = Modifier.size(200.dp * scale.value)) {
                drawCircle(
                    color = Color(0xFF42A5F5).copy(alpha = 0.3f),
                    radius = size.minDimension / 2
                )
                drawCircle(
                    color = Color(0xFF1A237E),
                    radius = size.minDimension / 2,
                    style = Stroke(width = 8.dp.toPx())
                )
            }
        }
        Spacer(Modifier.height(32.dp))
        Button(onClick = onDone) { Text("Done") }
    }
}

@Composable
private fun DistractionTimer(onDone: () -> Unit) {
    var secondsLeft by remember { mutableIntStateOf(5 * 60) }
    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }
    val minutes = secondsLeft / 60
    val secs = secondsLeft % 60

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Ride it out", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "%d:%02d".format(minutes, secs),
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "This feeling will pass. You don't have to act on it.",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))
        Button(onClick = onDone) { Text(if (secondsLeft == 0) "I made it" else "Stop early") }
    }
}

@Composable
private fun GroundingExercise(onDone: () -> Unit) {
    val steps = listOf(
        "5 things you can SEE",
        "4 things you can TOUCH",
        "3 things you can HEAR",
        "2 things you can SMELL",
        "1 thing you can TASTE"
    )
    var index by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("5-4-3-2-1 Grounding", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))
        Text(steps[index], style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))
        if (index < steps.lastIndex) {
            Button(onClick = { index++ }) { Text("Next") }
        } else {
            Button(onClick = onDone) { Text("I feel more grounded") }
        }
    }
}

@Composable
private fun ReasonsList(onDone: () -> Unit) {
    val reasons = listOf(
        "I deserve peace",
        "This relationship was not healthy for me",
        "I am rebuilding a better life",
        "My future self will thank me"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text("Reasons I'm better off", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        reasons.forEach { reason ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(reason, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) { Text("Done") }
    }
}

@Composable
private fun DontTextCountdown(onDone: () -> Unit) {
    var secondsLeft by remember { mutableIntStateOf(10 * 60) }
    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }
    val minutes = secondsLeft / 60
    val secs = secondsLeft % 60

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Don't text them", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))
        Text(
            "Wait it out. The urge will fade.",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
        Text(
            "%d:%02d".format(minutes, secs),
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(Modifier.height(32.dp))
        Button(onClick = onDone) { Text(if (secondsLeft == 0) "I didn't text" else "I'm still holding on") }
    }
}
