package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.data_source.settings.SettingsLocalDataSource
import com.kuro.notiflow.data.mapper.toDomain
import com.kuro.notiflow.data.mapper.toEntity
import com.kuro.notiflow.domain.api.settings.SettingsMenuRepository
import com.kuro.notiflow.domain.models.settings.SettingsModel
import com.kuro.notiflow.domain.utils.wrap
import com.kuro.notiflow.domain.utils.wrapFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsMenuRepositoryImpl @Inject constructor(
    private val dataSource: SettingsLocalDataSource
) : SettingsMenuRepository {
    override suspend fun updateSettings(settings: SettingsModel): Result<Int> = wrap {
        dataSource.updateSettings(settings.toEntity())
    }

    override suspend fun fetchAllSettings(): Flow<Result<SettingsModel>> = wrapFlow {
        dataSource.fetchSettingsFlow().map { it.toDomain() }
    }

    override suspend fun resetAllSettings(): Result<Int> = wrap {
        dataSource.updateSettings(SettingsModel().toEntity())
    }
}