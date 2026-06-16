package com.morningsun.app.domain.model

enum class ThemeMode {
    LIGHT,
    DARK
}

enum class LanguageMode {
    EN,
    ZH
}

data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.DARK,
    val languageMode: LanguageMode = LanguageMode.EN
)
