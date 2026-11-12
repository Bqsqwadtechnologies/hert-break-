package com.bqsqwad.heartbreakhealer.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    onNavigateToSupport: () -> Unit,
    notificationsEnabled: Boolean,
    reduceMotionEnabled: Boolean,
    onNotificationsChanged: (Boolean) -> Unit,
    onReduceMotionChanged: (Boolean) -> Unit,
    onClearData: () -> Unit,
    onBack: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    var showClearDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        SettingsItem("Theme · White") { }
        ListItem(headlineContent = { Text("Daily reminders") }, supportingContent = { Text("A gentle daily check-in notification") }, trailingContent = { Switch(checked = notificationsEnabled, onCheckedChange = onNotificationsChanged) })
        ListItem(headlineContent = { Text("Reduce motion") }, supportingContent = { Text("Limit animated transitions") }, trailingContent = { Switch(checked = reduceMotionEnabled, onCheckedChange = onReduceMotionChanged) })
        SettingsItem("Need more support?") { onNavigateToSupport() }
        HorizontalDivider(Modifier.padding(vertical = 12.dp))
        SettingsItem("Privacy Policy") {
            uriHandler.openUri("https://bqsqwadtechnologies.github.io/hert-break-/privacy.html")
        }
        SettingsItem("Terms of Service") {
            uriHandler.openUri("https://bqsqwadtechnologies.github.io/hert-break-/terms.html")
        }
        SettingsItem("Feedback") {
            uriHandler.openUri("mailto:bqsqwadtechnologies@bqsqwadai.com")
        }
        SettingsItem("Open-source licenses") { }
        HorizontalDivider(Modifier.padding(vertical = 12.dp))
        SettingsItem("Clear all data", destructive = true) { showClearDialog = true }
        Spacer(Modifier.height(24.dp))
        TextButton(onClick = onBack) { Text("Back") }
    }
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear all app data?") },
            text = { Text("This permanently removes your saved journal, letters, progress, and preferences from this device.") },
            confirmButton = { TextButton(onClick = { showClearDialog = false; onClearData() }) { Text("Clear data", color = MaterialTheme.colorScheme.error) } },
            dismissButton = { TextButton(onClick = { showClearDialog = false }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun SettingsItem(title: String, destructive: Boolean = false, onClick: () -> Unit) {
    ListItem(
        headlineContent = {
            Text(
                title,
                color = if (destructive) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurface
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}
