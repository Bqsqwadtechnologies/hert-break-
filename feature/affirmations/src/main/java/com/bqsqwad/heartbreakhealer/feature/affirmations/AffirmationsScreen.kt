package com.bqsqwad.heartbreakhealer.feature.affirmations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val sampleAffirmations = listOf(
    "I deserve peace.",
    "This feeling will pass.",
    "I am rebuilding a life I love.",
    "My worth is not defined by someone else.",
    "I choose kindness toward myself today.",
    "Healing is not linear, and that is okay.",
    "I am allowed to take up space.",
    "Every day I grow a little stronger."
)

@Composable
fun AffirmationsScreen(onBack: () -> Unit = {}) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Daily Affirmations", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = onBack) { Text("Back") }
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(sampleAffirmations) { _, text ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(text = text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
