package com.bqsqwad.heartbreakhealer.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class TriggerUi(val id: Long, val name: String, val coping: String)

@Composable
fun TriggersScreen(
    triggers: List<TriggerUi> = emptyList(),
    onAdd: (String, String) -> Unit = { _, _ -> },
    onBack: () -> Unit = {}
) {
    var adding by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var coping by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Triggers", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = onBack) { Text("Back") }
        }
        Text("Name a trigger and a coping action.", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(12.dp))
        if (adding) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Trigger") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = coping, onValueChange = { coping = it }, label = { Text("Coping action") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    if (name.isNotBlank()) {
                        onAdd(name, coping)
                        name = ""; coping = ""; adding = false
                    }
                }) { Text("Save") }
                OutlinedButton(onClick = { adding = false }) { Text("Cancel") }
            }
        } else {
            Button(onClick = { adding = true }, modifier = Modifier.fillMaxWidth()) { Text("Add trigger") }
            Spacer(Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(triggers, key = { it.id }) { t ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(t.name, style = MaterialTheme.typography.titleMedium)
                            Text(t.coping, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
