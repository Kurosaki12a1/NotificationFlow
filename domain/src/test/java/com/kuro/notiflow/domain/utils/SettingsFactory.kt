package com.kuro.notiflow.domain.utils

import com.kuro.notiflow.domain.models.settings.ColorType
import com.kuro.notiflow.domain.models.settings.LanguageType
import com.kuro.notiflow.domain.models.settings.SettingsModel
import com.kuro.notiflow.domain.models.settings.ThemeType

object SettingsFactory {
    fun model(
        language: LanguageType = LanguageType.DEFAULT,
        themeType: ThemeType = ThemeType.DEFAULT,
        colorsType: ColorType = ColorType.BLUE,
        isDynamicColorEnabled: Boolean = false,
        secureMode: Boolean = false
    ) = SettingsModel(
        language = language,
        themeType = themeType,
        colorsType = colorsType,
        isDynamicColorEnabled = isDynamicColorEnabled,
        secureMode = secureMode
    )
}
