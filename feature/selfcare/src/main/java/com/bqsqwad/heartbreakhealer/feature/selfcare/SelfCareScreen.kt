package com.bqsqwad.heartbreakhealer.feature.selfcare

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class CareTask(val id: String, val title: String, val done: Boolean)

@Composable
fun SelfCareScreen(
    tasks: List<CareTask> = listOf(
        CareTask("walk", "Take a short walk", false),
        CareTask("water", "Drink water", false),
        CareTask("sleep", "Protect your sleep", false),
        CareTask("friend", "Message a friend", false),
        CareTask("food", "Eat something nourishing", false)
    ),
    onToggle: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Self-Care", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = onBack) { Text("Back") }
        }
        Text("Small steps. Hearts for completed tasks.", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(tasks, key = { it.id }) { task ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(task.title, style = MaterialTheme.typography.bodyLarge)
                        Checkbox(checked = task.done, onCheckedChange = { onToggle(task.id) })
                    }
                }
            }
        }
    }
}
