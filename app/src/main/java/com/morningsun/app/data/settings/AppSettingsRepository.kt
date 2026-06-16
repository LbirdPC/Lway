package com.morningsun.app.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.morningsun.app.domain.model.AppSettings
import com.morningsun.app.domain.model.LanguageMode
import com.morningsun.app.domain.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.appSettingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "app_settings"
)

@Singleton
class AppSettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val themeModeKey = stringPreferencesKey("theme_mode")
    private val languageModeKey = stringPreferencesKey("language_mode")

    val settings: Flow<AppSettings> = context.appSettingsDataStore.data.map { preferences ->
        AppSettings(
            themeMode = preferences[themeModeKey]?.let(::parseThemeMode) ?: ThemeMode.DARK,
            languageMode = preferences[languageModeKey]?.let(::parseLanguageMode) ?: LanguageMode.EN
        )
    }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[themeModeKey] = themeMode.name
        }
    }

    suspend fun setLanguageMode(languageMode: LanguageMode) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[languageModeKey] = languageMode.name
        }
    }

    private fun parseThemeMode(value: String): ThemeMode =
        runCatching { ThemeMode.valueOf(value) }.getOrDefault(ThemeMode.DARK)

    private fun parseLanguageMode(value: String): LanguageMode =
        runCatching { LanguageMode.valueOf(value) }.getOrDefault(LanguageMode.EN)
}
