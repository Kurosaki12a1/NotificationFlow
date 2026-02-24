package com.kuro.notiflow.data.utils

import com.kuro.notiflow.data.data_source.entity.SettingsEntity
import com.kuro.notiflow.domain.Constants
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
        secureMode: Boolean = false,
        dataRetentionDays: Int = Constants.Settings.DEFAULT_RETENTION_DAYS
    ) = SettingsModel(
        language = language,
        themeType = themeType,
        colorsType = colorsType,
        isDynamicColorEnabled = isDynamicColorEnabled,
        secureMode = secureMode,
        dataRetentionDays = dataRetentionDays
    )

    fun entity(
        id: Int = Constants.Database.SETTINGS_ID,
        language: LanguageType = LanguageType.DEFAULT,
        themeType: ThemeType = ThemeType.DEFAULT,
        colorsType: ColorType = ColorType.BLUE,
        isDynamicColorEnabled: Boolean = false,
        secureMode: Boolean = false,
        dataRetentionDays: Int = Constants.Settings.DEFAULT_RETENTION_DAYS
    ) = SettingsEntity(
        id = id,
        language = language,
        themeColors = themeType,
        colorsType = colorsType,
        isDynamicColorEnable = isDynamicColorEnabled,
        secureMode = secureMode,
        dataRetentionDays = dataRetentionDays
    )
}
