package com.bqsqwad.heartbreakhealer.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    onNavigateToSupport: () -> Unit,
    onClearData: () -> Unit,
    onBack: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        SettingsItem("Theme") { }
        SettingsItem("Notifications") { }
        SettingsItem("Reduce motion") { }
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
        SettingsItem("Clear all data", destructive = true) { onClearData() }
        Spacer(Modifier.height(24.dp))
        TextButton(onClick = onBack) { Text("Back") }
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
