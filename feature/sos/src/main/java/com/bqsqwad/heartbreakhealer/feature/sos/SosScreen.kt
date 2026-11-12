package com.bqsqwad.heartbreakhealer.feature.sos

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SosScreen(onNavigateToSupport: () -> Unit, onClose: () -> Unit, onUrgePassed: () -> Unit = {}) {
    var mode by remember { mutableStateOf(0) }
    when (mode) {
        0 -> Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Urge SOS", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(8.dp))
            Text(
                "You're stronger than this moment. Pick something that helps.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            listOf(
                "Breathe with me" to 1,
                "5-minute distraction" to 2,
                "Grounding exercise" to 3,
                "Reasons I'm better off" to 4,
                "Don't text them" to 5
            ).forEach { (title, m) ->
                Card(onClick = { mode = m }, Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Text(title, Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                }
            }
            Spacer(Modifier.height(16.dp))
            OutlinedButton(onClick = onNavigateToSupport, Modifier.fillMaxWidth()) { Text("Need more support?") }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onClose, Modifier.fillMaxWidth()) { Text("I'm okay for now") }
        }
        1 -> Breathing(onDone = { mode = 0 })
        2 -> Timer5(onDone = { mode = 0 }, onPassed = onUrgePassed)
        3 -> Grounding(onDone = { onUrgePassed(); mode = 0 })
        4 -> Reasons(onDone = { mode = 0 })
        5 -> DontText(onDone = { mode = 0 }, onPassed = onUrgePassed)
    }
}

@Composable
private fun Breathing(onDone: () -> Unit) {
    val scale = remember { Animatable(0.6f) }
    var phase by remember { mutableStateOf("Inhale") }
    LaunchedEffect(Unit) {
        while (true) {
            phase = "Inhale"; scale.animateTo(1f, tween(4000, easing = LinearEasing))
            phase = "Hold"; delay(2000)
            phase = "Exhale"; scale.animateTo(0.6f, tween(4000, easing = LinearEasing))
            delay(1000)
        }
    }
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(phase, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))
        Box(Modifier.size(200.dp), contentAlignment = Alignment.Center) {
            Canvas(Modifier.size((200 * scale.value).dp)) {
                drawCircle(Color(0xFF42A5F5).copy(0.3f), size.minDimension / 2)
                drawCircle(Color(0xFF1A237E), size.minDimension / 2, style = Stroke(8.dp.toPx()))
            }
        }
        Spacer(Modifier.height(32.dp))
        Button(onClick = onDone) { Text("Done") }
    }
}

@Composable
private fun Timer5(onDone: () -> Unit, onPassed: () -> Unit) {
    var left by remember { mutableIntStateOf(300) }
    LaunchedEffect(Unit) { while (left > 0) { delay(1000); left-- } }
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Ride it out", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text("%d:%02d".format(left / 60, left % 60), style = MaterialTheme.typography.displayLarge)
        Spacer(Modifier.height(16.dp))
        Text("This feeling will pass.", textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))
        Button(onClick = { if (left == 0) onPassed(); onDone() }) { Text(if (left == 0) "I made it" else "Stop early") }
    }
}

@Composable
private fun Grounding(onDone: () -> Unit) {
    val steps = listOf("5 things you can SEE", "4 things you can TOUCH", "3 things you can HEAR", "2 things you can SMELL", "1 thing you can TASTE")
    var i by remember { mutableIntStateOf(0) }
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("5-4-3-2-1 Grounding", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))
        Text(steps[i], style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))
        if (i < steps.lastIndex) Button(onClick = { i++ }) { Text("Next") }
        else Button(onClick = onDone) { Text("I feel more grounded") }
    }
}

@Composable
private fun Reasons(onDone: () -> Unit) {
    val list = listOf("I deserve peace", "This relationship was not healthy for me", "I am rebuilding a better life", "My future self will thank me")
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp)) {
        Text("Reasons I'm better off", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        list.forEach {
            Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                Text(it, Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = onDone, Modifier.fillMaxWidth()) { Text("Done") }
    }
}

@Composable
private fun DontText(onDone: () -> Unit, onPassed: () -> Unit) {
    var left by remember { mutableIntStateOf(600) }
    LaunchedEffect(Unit) { while (left > 0) { delay(1000); left-- } }
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Don't text them", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))
        Text("Wait it out. The urge will fade.", textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        Text("%d:%02d".format(left / 60, left % 60), style = MaterialTheme.typography.displayLarge)
        Spacer(Modifier.height(32.dp))
        Button(onClick = { if (left == 0) onPassed(); onDone() }) { Text(if (left == 0) "I didn't text" else "I'm still holding on") }
    }
}
