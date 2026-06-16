package com.morningsun.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.morningsun.app.presentation.localization.LocalAppStrings
import com.morningsun.app.presentation.localization.stringsFor
import com.morningsun.app.presentation.navigation.MorningSunNavHost
import com.morningsun.app.presentation.ui.theme.MorningSunTheme
import com.morningsun.app.presentation.viewmodel.AppSettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: AppSettingsViewModel = hiltViewModel()
            val settings by settingsViewModel.settings.collectAsState()

            MorningSunTheme(themeMode = settings.themeMode) {
                CompositionLocalProvider(
                    LocalAppStrings provides stringsFor(settings.languageMode)
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MorningSunNavHost(
                            languageMode = settings.languageMode,
                            themeMode = settings.themeMode,
                            onThemeModeChange = settingsViewModel::setThemeMode,
                            onLanguageModeChange = settingsViewModel::setLanguageMode
                        )
                    }
                }
            }
        }
    }
}
