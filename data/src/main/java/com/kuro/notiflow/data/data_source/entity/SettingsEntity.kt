package com.kuro.notiflow.data.data_source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.models.settings.ColorType
import com.kuro.notiflow.domain.models.settings.LanguageType
import com.kuro.notiflow.domain.models.settings.ThemeType

@Entity(tableName = Constants.Database.SETTINGS_TABLE)
data class SettingsEntity(
    @PrimaryKey(autoGenerate = false) val id: Int = Constants.Database.SETTINGS_ID,
    @ColumnInfo(Constants.Database.COLUMN_LANGUAGE) val language: LanguageType,
    @ColumnInfo(Constants.Database.COLUMN_THEME_COLORS) val themeColors: ThemeType,
    @ColumnInfo(Constants.Database.COLUMN_COLORS_TYPE) val colorsType: ColorType,
    @ColumnInfo(Constants.Database.COLUMN_DYNAMIC_COLOR) val isDynamicColorEnable: Boolean,
    @ColumnInfo(Constants.Database.COLUMN_SECURE_MODE) val secureMode: Boolean,
    @ColumnInfo(Constants.Database.COLUMN_DATA_RETENTION_DAYS) val dataRetentionDays: Int
)
