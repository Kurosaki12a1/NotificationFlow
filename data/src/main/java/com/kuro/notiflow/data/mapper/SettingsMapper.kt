package com.kuro.notiflow.data.mapper

import com.kuro.notiflow.data.entity.SettingsEntity
import com.kuro.notiflow.domain.models.settings.SettingsModel

fun SettingsEntity.toDomain(): SettingsModel = SettingsModel(
    language = this.language,
    themeType = this.themeColors,
    colorsType = this.colorsType,
    isDynamicColorEnabled = this.isDynamicColorEnable,
    secureMode = this.secureMode,
    dataRetentionDays = this.dataRetentionDays
)

fun SettingsModel.toEntity(): SettingsEntity = SettingsEntity(
    id = 1, // Always id = 1
    language = this.language,
    themeColors = this.themeType,
    colorsType = this.colorsType,
    isDynamicColorEnable = this.isDynamicColorEnabled,
    secureMode = this.secureMode,
    dataRetentionDays = this.dataRetentionDays
)
