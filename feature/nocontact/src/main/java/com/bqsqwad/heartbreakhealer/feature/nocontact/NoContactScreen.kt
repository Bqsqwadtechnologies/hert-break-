package com.bqsqwad.heartbreakhealer.feature.nocontact

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bqsqwad.heartbreakhealer.core.common.ads.AdsConfig
import com.bqsqwad.heartbreakhealer.core.ui.ads.BannerAdView

@Composable
fun NoContactScreen(
    currentDays: Int = 0,
    bestDays: Int = 0,
    onSlip: () -> Unit = {},
    adsAllowed: Boolean = false,
    onBack: () -> Unit = {}
) {
    var showSlipDialog by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("No-Contact", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("A slip is okay. Restart gently.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))
        Text("$currentDays", style = MaterialTheme.typography.displayLarge)
        Text("days", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text("Best streak: $bestDays days", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(32.dp))
        OutlinedButton(onClick = { showSlipDialog = true }) { Text("I slipped — restart gently") }
        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onBack) { Text("Back") }
        Spacer(Modifier.height(8.dp))
        BannerAdView(unitId = AdsConfig.BANNER_LIBRARY, enabled = adsAllowed)
    }
    if (showSlipDialog) {
        AlertDialog(
            onDismissRequest = { showSlipDialog = false },
            title = { Text("It's okay") },
            text = { Text("A slip doesn't erase your progress. We'll restart the streak gently from today. You're still healing.") },
            confirmButton = { Button(onClick = { onSlip(); showSlipDialog = false }) { Text("Restart gently") } },
            dismissButton = { TextButton(onClick = { showSlipDialog = false }) { Text("Cancel") } }
        )
    }
}
