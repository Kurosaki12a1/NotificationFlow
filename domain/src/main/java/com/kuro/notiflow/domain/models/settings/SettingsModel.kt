package com.kuro.notiflow.domain.models.settings

import com.kuro.notiflow.domain.Constants

data class SettingsModel(
    val language: LanguageType = LanguageType.DEFAULT,
    val themeType: ThemeType = ThemeType.DEFAULT,
    val colorsType: ColorType = ColorType.BLUE,
    val isDynamicColorEnabled: Boolean = false,
    val secureMode: Boolean = false,
    val dataRetentionDays: Int = Constants.Settings.DEFAULT_RETENTION_DAYS
)
