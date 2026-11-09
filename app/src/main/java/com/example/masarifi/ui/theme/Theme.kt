package com.example.masarifi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF3F51B5),
    secondary = androidx.compose.ui.graphics.Color(0xFF00BCD4),
    tertiary = androidx.compose.ui.graphics.Color(0xFFFFC107)
)

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF7986CB),
    secondary = androidx.compose.ui.graphics.Color(0xFF4DD0E1),
    tertiary = androidx.compose.ui.graphics.Color(0xFFFFD54F)
)

@Composable
fun MasarifiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
