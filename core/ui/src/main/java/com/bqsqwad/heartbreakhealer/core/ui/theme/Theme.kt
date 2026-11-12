package com.bqsqwad.heartbreakhealer.core.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val HeartbreakLightScheme = lightColorScheme(
    primary = Color(0xFFE51D3E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE5EA),
    onPrimaryContainer = Color(0xFF7A1024),
    secondary = Color(0xFF2457D6),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8EEFF),
    onSecondaryContainer = Color(0xFF142B70),
    tertiary = Color(0xFFFF8AA0),
    onTertiary = Color(0xFF4B1020),
    background = Color.White,
    onBackground = Color(0xFF17172A),
    surface = Color.White,
    onSurface = Color(0xFF17172A),
    surfaceVariant = Color(0xFFFFF1F4),
    onSurfaceVariant = Color(0xFF55536A),
    outline = Color(0xFFE8E4EA)
)

@Composable
fun HeartbreakHealerTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(colorScheme = HeartbreakLightScheme, typography = Typography, content = content)
}
