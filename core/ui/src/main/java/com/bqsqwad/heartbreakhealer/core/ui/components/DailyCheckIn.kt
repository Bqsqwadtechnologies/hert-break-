package com.bqsqwad.heartbreakhealer.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DailyCheckIn(
    onSubmit: (mood: Int, note: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var mood by remember { mutableIntStateOf(3) }
    var note by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Daily check-in", style = MaterialTheme.typography.titleMedium)
            if (submitted) {
                Spacer(Modifier.height(8.dp))
                Text("Saved. +5 hearts.", color = MaterialTheme.colorScheme.primary)
            } else {
                Spacer(Modifier.height(8.dp))
                Text("How are you feeling? (1–5)")
                Slider(
                    value = mood.toFloat(),
                    onValueChange = { mood = it.toInt() },
                    valueRange = 1f..5f,
                    steps = 3
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Optional note") },
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    onSubmit(mood, note)
                    submitted = true
                }) { Text("Check in") }
            }
        }
    }
}
