package com.bqsqwad.heartbreakhealer.feature.selfcare

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bqsqwad.heartbreakhealer.core.common.ads.AdsConfig
import com.bqsqwad.heartbreakhealer.core.ui.ads.BannerAdView

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
    val taskState = remember { mutableStateListOf<CareTask>().apply { addAll(tasks) } }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Self-Care", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
            TextButton(onClick = onBack) { Text("Back") }
        }
        Text("Small steps. Hearts for completed tasks.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(Modifier.padding(16.dp)) {
                Text("${taskState.count { it.done }} of ${taskState.size} completed", style = MaterialTheme.typography.titleMedium)
                LinearProgressIndicator(progress = { if (taskState.isEmpty()) 0f else taskState.count { it.done }.toFloat() / taskState.size }, modifier = Modifier.fillMaxWidth().padding(top = 10.dp))
            }
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(taskState, key = { it.id }) { task ->
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Row(Modifier.padding(14.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(task.title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                        Checkbox(checked = task.done, onCheckedChange = {
                            val index = taskState.indexOfFirst { item -> item.id == task.id }
                            if (index >= 0) taskState[index] = task.copy(done = it)
                            if (it) onToggle(task.id)
                        })
                    }
                }
            }
        }
        Text("Support this free healing space", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        BannerAdView(unitId = AdsConfig.BANNER_LIST)
    }
}
