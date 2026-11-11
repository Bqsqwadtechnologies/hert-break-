package com.bqsqwad.heartbreakhealer.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bqsqwad.heartbreakhealer.core.domain.model.BoundaryScripts
import com.bqsqwad.heartbreakhealer.core.domain.model.CopingDeck
import com.bqsqwad.heartbreakhealer.core.domain.model.HealingStage
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun StageTrackerCard(stage: HealingStage, days: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Where you are", style = MaterialTheme.typography.labelLarge)
            Text(stage.label, style = MaterialTheme.typography.titleLarge)
            Text(stage.hint, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(4.dp))
            Text("Day $days of your healing path", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun UrgePassedCard(count: Int, onLogged: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Urges you rode out", style = MaterialTheme.typography.titleMedium)
            Text("$count", style = MaterialTheme.typography.displaySmall)
            Text("Each one builds self-trust.", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Button(onClick = onLogged) { Text("I rode this one out") }
        }
    }
}

@Composable
fun CopingCardsScreen(onBack: () -> Unit) {
    var index by remember { mutableIntStateOf((0 until CopingDeck.cards.size).random()) }
    val card = CopingDeck.cards[index]
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Coping card", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(card.title, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
                Text(card.body, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = { index = (index + 1) % CopingDeck.cards.size }) { Text("Another card") }
        TextButton(onClick = onBack) { Text("Back") }
    }
}

@Composable
fun BoundaryScriptsScreen(onBack: () -> Unit) {
    val clipboard = LocalClipboardManager.current
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("Boundary scripts", style = MaterialTheme.typography.headlineMedium)
        Text("Tap to copy. Soft, clear, no debate.", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(12.dp))
        BoundaryScripts.scripts.forEach { (text, whenTo) ->
            Card(
                onClick = { clipboard.setText(AnnotatedString(text)) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(text, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(4.dp))
                    Text(whenTo, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        TextButton(onClick = onBack) { Text("Back") }
    }
}

@Composable
fun TimeCapsuleScreen(
    existing: String? = null,
    unlockDate: String? = null,
    onSave: (String, Int) -> Unit,
    onBack: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    var days by remember { mutableIntStateOf(30) }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Letter to future you", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        if (existing != null && unlockDate != null) {
            Text("Sealed until $unlockDate", style = MaterialTheme.typography.bodyMedium)
            if (LocalDate.now().toString() >= unlockDate) {
                Text(existing, style = MaterialTheme.typography.bodyLarge)
            } else {
                Text("Still sealed. Be patient with yourself.")
            }
        } else {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth().height(180.dp),
                placeholder = { Text("Dear future me…") }
            )
            Spacer(Modifier.height(8.dp))
            Text("Open after:")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(30, 60, 90).forEach { d ->
                    FilterChip(selected = days == d, onClick = { days = d }, label = { Text("$d days") })
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { if (text.isNotBlank()) onSave(text, days) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Seal letter") }
        }
        TextButton(onClick = onBack) { Text("Back") }
    }
}

@Composable
fun GriefVsContactScreen(onBack: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Lonely, or missing them?", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        when (step) {
            0 -> {
                Text("Do you want connection with anyone kind, or specifically with your ex?", textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Button(onClick = { step = 1 }, modifier = Modifier.fillMaxWidth()) { Text("Anyone kind / I feel lonely") }
                OutlinedButton(onClick = { step = 2 }, modifier = Modifier.fillMaxWidth()) { Text("Specifically them") }
            }
            1 -> {
                Text("Loneliness is human. Reach toward safe people — a friend, family, a walk outside — not the door that hurt you.", textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onBack) { Text("Okay") }
            }
            2 -> {
                Text("Missing them is part of attachment. The urge will rise and fall. You can feel it without acting on it. Try a coping card or the 10-minute delay.", textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onBack) { Text("I'll ride it out") }
            }
        }
    }
}

@Composable
fun SmallWinsScreen(wins: List<String>, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Small wins", style = MaterialTheme.typography.headlineMedium)
        Text("Proof you're showing up for yourself.", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(12.dp))
        if (wins.isEmpty()) {
            Text("Wins will appear as you check in, use SOS, and keep no-contact.")
        } else {
            wins.forEach { w ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text(w, Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        TextButton(onClick = onBack) { Text("Back") }
    }
}

@Composable
fun EveningWindDownScreen(onSave: (gratitude: String, release: String, hope: String) -> Unit, onBack: () -> Unit) {
    var g by remember { mutableStateOf("") }
    var r by remember { mutableStateOf("") }
    var h by remember { mutableStateOf("") }
    val isEvening = LocalTime.now().hour >= 20
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("Close the day", style = MaterialTheme.typography.headlineMedium)
        if (isEvening) Text("Evening is hard for many people. Soft landing.", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(g, { g = it }, label = { Text("One thing I'm grateful for") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(r, { r = it }, label = { Text("One thing I release today") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(h, { h = it }, label = { Text("One small hope for tomorrow") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        Button(onClick = { onSave(g, r, h); onBack() }, modifier = Modifier.fillMaxWidth()) { Text("Save & rest") }
        TextButton(onClick = onBack) { Text("Back") }
    }
}

@Composable
fun MoodInsightCard(avgMood: Float?, harderTimeHint: String?) {
    if (avgMood == null) return
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Gentle pattern", style = MaterialTheme.typography.titleMedium)
            Text("Average check-in: ${"%.1f".format(avgMood)} / 5", style = MaterialTheme.typography.bodyMedium)
            harderTimeHint?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
