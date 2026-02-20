package com.kuro.notiflow.domain.models.settings

data class SettingsModel(
    val language: LanguageType = LanguageType.DEFAULT,
    val themeType: ThemeType = ThemeType.DEFAULT,
    val colorsType: ColorType = ColorType.BLUE,
    val isDynamicColorEnabled: Boolean = false,
    val secureMode: Boolean = false,
    val dataRetentionDays: Int = 90
)
