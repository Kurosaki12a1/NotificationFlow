package com.kuro.notiflow.data.data_source.settings.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kuro.notiflow.domain.models.settings.ColorType
import com.kuro.notiflow.domain.models.settings.LanguageType
import com.kuro.notiflow.domain.models.settings.ThemeType


@Entity(tableName = "settings_table")
data class SettingsEntity(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    @ColumnInfo("language") val language: LanguageType = LanguageType.DEFAULT,
    @ColumnInfo("theme_colors") val themeColors: ThemeType = ThemeType.DEFAULT,
    @ColumnInfo("colors_type") val colorsType: ColorType = ColorType.BLUE,
    @ColumnInfo("dynamic_color") val isDynamicColorEnable: Boolean = false,
)