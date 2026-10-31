package com.bqsqwad.heartbreakhealer.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bqsqwad.heartbreakhealer.core.domain.model.Goal
import java.time.LocalDate

@Composable
fun OnboardingScreen(
    onComplete: (nickname: String, breakupDate: LocalDate, goals: Set<Goal>) -> Unit
) {
    var step by remember { mutableStateOf(0) }
    var nickname by remember { mutableStateOf("") }
    var goals by remember { mutableStateOf(setOf<Goal>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (step) {
            0 -> {
                Text(
                    text = "Welcome",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "A private space to heal, one day at a time.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = { step = 1 },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continue")
                }
            }
            1 -> {
                Text(
                    text = "What should we call you?",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Nickname") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { if (nickname.isNotBlank()) step = 2 },
                    enabled = nickname.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continue")
                }
            }
            2 -> {
                Text(
                    text = "What are you focusing on?",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.height(16.dp))
                Goal.entries.forEach { goal ->
                    val selected = goal in goals
                    FilterChip(
                        selected = selected,
                        onClick = {
                            goals = if (selected) goals - goal else goals + goal
                        },
                        label = {
                            Text(
                                when (goal) {
                                    Goal.HEAL -> "Heal"
                                    Goal.NO_CONTACT -> "Stay no-contact"
                                    Goal.REBUILD -> "Rebuild my life"
                                    Goal.CLOSURE -> "Find closure"
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        if (goals.isNotEmpty()) {
                            onComplete(nickname, LocalDate.now(), goals)
                        }
                    },
                    enabled = goals.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Start healing")
                }
            }
        }
    }
}
