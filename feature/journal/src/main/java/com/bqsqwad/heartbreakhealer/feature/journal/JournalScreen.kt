package com.bqsqwad.heartbreakhealer.feature.journal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class JournalUiEntry(val id: Long, val date: String, val mood: Int, val content: String)

@Composable
fun JournalScreen(
    entries: List<JournalUiEntry> = emptyList(),
    onSaveEntry: (mood: Int, content: String) -> Unit = { _, _ -> },
    onBack: () -> Unit = {}
) {
    var showEditor by remember { mutableStateOf(false) }
    var mood by remember { mutableIntStateOf(3) }
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Private Journal", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = onBack) { Text("Back") }
        }
        Text("Only on this device. Optional lock available in Settings.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        if (showEditor) {
            Text("How are you feeling? (1–5)", style = MaterialTheme.typography.titleMedium)
            Slider(value = mood.toFloat(), onValueChange = { mood = it.toInt() }, valueRange = 1f..5f, steps = 3)
            OutlinedTextField(value = text, onValueChange = { text = it }, modifier = Modifier.fillMaxWidth().height(160.dp), placeholder = { Text("Write freely…") })
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { if (text.isNotBlank()) { onSaveEntry(mood, text); text = ""; showEditor = false } }) { Text("Save") }
                OutlinedButton(onClick = { showEditor = false }) { Text("Cancel") }
            }
        } else {
            Button(onClick = { showEditor = true }, modifier = Modifier.fillMaxWidth()) { Text("New entry") }
            Spacer(Modifier.height(12.dp))
            if (entries.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No entries yet. Your words stay private.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(entries, key = { it.id }) { entry ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(12.dp)) {
                                Text("${entry.date} · Mood ${entry.mood}/5", style = MaterialTheme.typography.labelLarge)
                                Spacer(Modifier.height(4.dp))
                                Text(entry.content, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
