package com.kuro.notiflow.domain.api.settings

import com.kuro.notiflow.domain.models.settings.SettingsModel
import kotlinx.coroutines.flow.Flow

interface SettingsMenuRepository {
    suspend fun updateSettings(settings: SettingsModel): Result<Int>
    suspend fun fetchAllSettings(): Flow<Result<SettingsModel>>
    suspend fun resetAllSettings(): Result<Int>
}