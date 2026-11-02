package com.bqsqwad.heartbreakhealer.feature.rebuild

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class RebuildDayUi(val day: Int, val title: String, val category: String, val done: Boolean)

@Composable
fun RebuildScreen(
    days: List<RebuildDayUi> = (1..30).map {
        RebuildDayUi(it, "Day $it challenge", listOf("Fitness","Hobby","Social","Confidence","Finance")[it % 5], false)
    },
    onCompleteDay: (Int) -> Unit = {},
    onBack: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("30-Day Rebuild", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = onBack) { Text("Back") }
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(days, key = { it.day }) { d ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Day ${d.day}: ${d.title}", style = MaterialTheme.typography.titleMedium)
                            Text(d.category, style = MaterialTheme.typography.bodySmall)
                        }
                        if (d.done) Text("Done", color = MaterialTheme.colorScheme.primary)
                        else Button(onClick = { onCompleteDay(d.day) }) { Text("Complete") }
                    }
                }
            }
        }
    }
}
