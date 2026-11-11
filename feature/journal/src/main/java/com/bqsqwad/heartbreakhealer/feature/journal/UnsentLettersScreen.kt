package com.bqsqwad.heartbreakhealer.feature.journal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class LetterUi(val id: Long, val content: String, val status: String)

@Composable
fun UnsentLettersScreen(
    letters: List<LetterUi> = emptyList(),
    onSave: (String) -> Unit = {},
    onRelease: (Long) -> Unit = {},
    onBack: () -> Unit = {}
) {
    var writing by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    Column(Modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Unsent Letters", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = onBack) { Text("Back") }
        }
        Text(
            "Write what you need to say. Then release it — no send button.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(12.dp))

        if (writing) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth().height(200.dp),
                placeholder = { Text("Dear...") }
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    if (text.isNotBlank()) {
                        onSave(text)
                        text = ""
                        writing = false
                    }
                }) { Text("Save draft") }
                OutlinedButton(onClick = { writing = false }) { Text("Cancel") }
            }
        } else {
            Button(onClick = { writing = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Write a letter")
            }
            Spacer(Modifier.height(12.dp))
            if (letters.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No letters yet.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(letters, key = { it.id }) { letter ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(12.dp)) {
                                Text(
                                    letter.content.take(120) + if (letter.content.length > 120) "…" else "",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(Modifier.height(8.dp))
                                if (letter.status == "DRAFT") {
                                    Button(onClick = { onRelease(letter.id) }) {
                                        Text("Release / Burn")
                                    }
                                } else {
                                    Text("Released", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
