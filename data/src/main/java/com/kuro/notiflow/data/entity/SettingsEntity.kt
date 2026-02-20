package com.kuro.notiflow.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kuro.notiflow.domain.models.settings.ColorType
import com.kuro.notiflow.domain.models.settings.LanguageType
import com.kuro.notiflow.domain.models.settings.ThemeType

@Entity(tableName = "settings_table")
data class SettingsEntity(
    @PrimaryKey(autoGenerate = false) val id: Int = 1,
    @ColumnInfo("language") val language: LanguageType,
    @ColumnInfo("theme_colors") val themeColors: ThemeType,
    @ColumnInfo("colors_type") val colorsType: ColorType,
    @ColumnInfo("dynamic_color") val isDynamicColorEnable: Boolean,
    @ColumnInfo("secure_mode") val secureMode: Boolean,
    @ColumnInfo("data_retention_days") val dataRetentionDays: Int
)
