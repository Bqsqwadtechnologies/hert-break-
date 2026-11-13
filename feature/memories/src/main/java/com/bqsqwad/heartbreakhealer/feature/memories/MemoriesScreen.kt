package com.bqsqwad.heartbreakhealer.feature.memories

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bqsqwad.heartbreakhealer.core.common.ads.AdsConfig
import com.bqsqwad.heartbreakhealer.core.ui.ads.BannerAdView

data class MemoryUi(val id: Long, val title: String, val note: String)

@Composable
fun MemoriesScreen(
    memories: List<MemoryUi> = emptyList(),
    onAdd: (String, String) -> Unit = { _, _ -> },
    adsAllowed: Boolean = false,
    onBack: () -> Unit = {}
) {
    var adding by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Memories Box", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = onBack) { Text("Back") }
        }
        Text(
            "Keep the good ones. Local only.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(12.dp))
        if (adding) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note") },
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    if (title.isNotBlank()) {
                        onAdd(title, note)
                        title = ""
                        note = ""
                        adding = false
                    }
                }) { Text("Save") }
                OutlinedButton(onClick = { adding = false }) { Text("Cancel") }
            }
        } else {
            Button(onClick = { adding = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Add a memory")
            }
            Spacer(Modifier.height(12.dp))
            if (memories.isEmpty()) {
                Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No memories saved yet.")
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(memories, key = { it.id }) { memory ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(12.dp)) {
                                Text(memory.title, style = MaterialTheme.typography.titleMedium)
                                if (memory.note.isNotBlank()) {
                                    Text(memory.note, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!adding) {
            Spacer(Modifier.height(8.dp))
            BannerAdView(unitId = AdsConfig.BANNER_LIBRARY, enabled = adsAllowed)
        }
    }
}
