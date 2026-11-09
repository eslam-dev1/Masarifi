package com.example.masarifi

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.masarifi.ui.MasarifiApp
import com.example.masarifi.ui.theme.MasarifiTheme

private const val PREFS = "masarifi_settings"
private const val DARK_MODE_KEY = "dark_mode"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        setContent {
            var darkMode by remember { mutableStateOf(preferences.getBoolean(DARK_MODE_KEY, false)) }
            MasarifiTheme(darkTheme = darkMode) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MasarifiApp(
                        darkMode = darkMode,
                        onToggleDarkMode = {
                            darkMode = !darkMode
                            preferences.edit().putBoolean(DARK_MODE_KEY, darkMode).apply()
                        }
                    )
                }
            }
        }
    }
}
