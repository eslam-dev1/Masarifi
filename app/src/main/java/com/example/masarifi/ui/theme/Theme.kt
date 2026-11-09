package com.example.masarifi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF173B32), onPrimary = Color.White,
    secondary = Color(0xFF6B9B7C), onSecondary = Color.White,
    tertiary = Color(0xFFE6A15A), background = Color(0xFFF7F8F4),
    onBackground = Color(0xFF173B32), surface = Color.White,
    onSurface = Color(0xFF173B32), surfaceVariant = Color(0xFFE9F2EC),
    onSurfaceVariant = Color(0xFF52625A), error = Color(0xFFBA1A1A)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB7E4C7), onPrimary = Color(0xFF173B32),
    secondary = Color(0xFF8BBF9D), tertiary = Color(0xFFE6A15A),
    background = Color(0xFF111C18), onBackground = Color(0xFFE7F0E9),
    surface = Color(0xFF1C2A24), onSurface = Color(0xFFE7F0E9),
    surfaceVariant = Color(0xFF2C3B34), onSurfaceVariant = Color(0xFFC1CEC5),
    error = Color(0xFFFFB4AB)
)

@Composable
fun MasarifiTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) DarkColors else LightColors, content = content)
}
