package com.kuro.notiflow.data.utils

import com.kuro.notiflow.data.entity.SettingsEntity
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

    fun entity(
        id: Int = 1,
        language: LanguageType = LanguageType.DEFAULT,
        themeType: ThemeType = ThemeType.DEFAULT,
        colorsType: ColorType = ColorType.BLUE,
        isDynamicColorEnabled: Boolean = false,
        secureMode: Boolean = false
    ) = SettingsEntity(
        id = id,
        language = language,
        themeColors = themeType,
        colorsType = colorsType,
        isDynamicColorEnable = isDynamicColorEnabled,
        secureMode = secureMode
    )
}
