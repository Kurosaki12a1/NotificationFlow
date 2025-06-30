package com.kuro.notiflow.data.mapper

import com.kuro.notiflow.data.entity.SettingsEntity
import com.kuro.notiflow.domain.models.settings.SettingsModel

fun SettingsEntity.toDomain() : SettingsModel = SettingsModel(
    language = this.language,
    themeColors = this.themeColors,
    colorsType = this.colorsType,
    isDynamicColorEnable = this.isDynamicColorEnable
)

fun SettingsModel.toEntity() : SettingsEntity = SettingsEntity(
    id = 1, // Always id = 1
    language = this.language,
    themeColors = this.themeColors,
    colorsType = this.colorsType,
    isDynamicColorEnable = this.isDynamicColorEnable
)