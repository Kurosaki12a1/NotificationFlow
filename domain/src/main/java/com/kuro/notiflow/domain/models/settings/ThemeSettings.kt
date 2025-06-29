package com.kuro.notiflow.domain.models.settings

data class ThemeSettings(
    val language: LanguageType = LanguageType.DEFAULT,
    val themeColors: ThemeType = ThemeType.DEFAULT,
    val colorsType: ColorType = ColorType.BLUE,
    val isDynamicColorEnable: Boolean = false,
)