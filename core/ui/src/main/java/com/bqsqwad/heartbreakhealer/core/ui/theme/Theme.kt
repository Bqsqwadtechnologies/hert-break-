package com.bqsqwad.heartbreakhealer.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = RoyalBlue, onPrimary = White,
    primaryContainer = LightContainer, onPrimaryContainer = DeepBlue,
    secondary = SkyBlue, onSecondary = White,
    tertiary = SoftHeartPink, onTertiary = White,
    background = LightSurface, onBackground = Black,
    surface = White, onSurface = Black,
    surfaceVariant = LightContainer, onSurfaceVariant = DeepBlue
)

private val DarkColorScheme = darkColorScheme(
    primary = SkyBlue, onPrimary = MidnightNavy,
    primaryContainer = DeepPlum, onPrimaryContainer = White,
    secondary = MintAccent, onSecondary = MidnightNavy,
    tertiary = WarmCoral, onTertiary = MidnightNavy,
    background = MidnightNavy, onBackground = White,
    surface = DarkSurface, onSurface = White,
    surfaceVariant = DarkContainer, onSurfaceVariant = MintAccent
)

@Composable
fun HeartbreakHealerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
