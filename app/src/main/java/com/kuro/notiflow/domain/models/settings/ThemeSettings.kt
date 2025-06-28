package com.kuro.notiflow.domain.models.settings

import com.kuro.notiflow.presentation.common.theme.ColorType
import com.kuro.notiflow.presentation.common.theme.LanguageType
import com.kuro.notiflow.presentation.common.theme.ThemeType

data class ThemeSettings(
    val language: LanguageType = LanguageType.DEFAULT,
    val themeColors: ThemeType = ThemeType.DEFAULT,
    val colorsType: ColorType = ColorType.BLUE,
    val isDynamicColorEnable: Boolean = false,
)