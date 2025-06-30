package com.kuro.notiflow.data.data_source.settings

import com.kuro.notiflow.data.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SettingsLocalDataSource {
    fun fetchSettingsFlow(): Flow<SettingsEntity>
    suspend fun updateSettings(settings: SettingsEntity) : Int
}

class SettingsLocalDataSourceImpl @Inject constructor(
    private val settingsDao: SettingsDao
) : SettingsLocalDataSource {
    override fun fetchSettingsFlow(): Flow<SettingsEntity> {
        return settingsDao.fetchSettingsFlow()
    }

    override suspend fun updateSettings(settings: SettingsEntity) : Int {
        return settingsDao.updateSettings(settings)
    }

}