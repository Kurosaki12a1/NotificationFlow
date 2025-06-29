package com.kuro.notiflow.domain.models.settings

import com.kuro.notiflow.domain.models.settings.ColorType
import com.kuro.notiflow.domain.models.settings.LanguageType
import com.kuro.notiflow.domain.models.settings.ThemeType

data class ThemeSettings(
    val language: LanguageType = LanguageType.DEFAULT,
    val themeColors: ThemeType = ThemeType.DEFAULT,
    val colorsType: ColorType = ColorType.BLUE,
    val isDynamicColorEnable: Boolean = false,
)