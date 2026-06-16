package com.morningsun.app.presentation.ui.theme

import android.app.Activity
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.morningsun.app.domain.model.ThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryVariant,
    secondary = Secondary,
    onSecondary = OnSecondary,
    tertiary = Accent,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnBackgroundDark,
    surfaceVariant = SurfaceLight,
    onSurfaceVariant = OnBackgroundLight,
    error = Error,
    onError = OnPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2F5F73),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD2EEF7),
    secondary = Color(0xFF6F5A24),
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF39785D),
    background = Color(0xFFF7F9F6),
    onBackground = Color(0xFF1B1D1A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1B1D1A),
    surfaceVariant = Color(0xFFE4E8E1),
    onSurfaceVariant = Color(0xFF44483F),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF)
)

@Composable
fun MorningSunTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit
) {
    val isLightTheme = themeMode == ThemeMode.LIGHT
    val colorScheme = if (isLightTheme) LightColorScheme else DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isLightTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = isLightTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
