package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.data_source.settings.SettingsLocalDataSource
import com.kuro.notiflow.data.mapper.toDomain
import com.kuro.notiflow.data.mapper.toEntity
import com.kuro.notiflow.domain.api.settings.SettingsMenuRepository
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.domain.models.settings.SettingsModel
import com.kuro.notiflow.domain.utils.wrap
import com.kuro.notiflow.domain.utils.wrapFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 *
 * This repository coordinates data between the [SettingsLocalDataSource] and the
 * domain layer. It handles the mapping between data entities and domain models
 * while ensuring all operations are safely wrapped in [Result] objects.
 *
 * @property dataSource The primary source of truth for user settings (e.g., Room or DataStore).
 */
class SettingsMenuRepositoryImpl @Inject constructor(
    private val dataSource: SettingsLocalDataSource
) : SettingsMenuRepository {

    /**
     * Updates the existing settings in the local storage.
     *
     * @param settings The [SettingsModel] representing the new state.
     * @return A [Result] containing the update status or an error.
     */
    override suspend fun updateSettings(settings: SettingsModel): Result<Int> = wrap {
        AppLog.i(TAG, "updateSettings")
        dataSource.updateSettings(settings.toEntity())
    }

    /**
     * Observes a stream of settings updates.
     *
     * Wraps the [Flow] to catch stream-level exceptions and convert them into
     * encapsulated [Result] objects for safe observation in the UI layer.
     *
     * @return A [Flow] emitting [Result] objects containing the [SettingsModel].
     */
    override suspend fun fetchAllSettings(): Flow<Result<SettingsModel>> = wrapFlow {
        AppLog.d(TAG, "fetchAllSettings")
        dataSource.fetchSettingsFlow().map { it.toDomain() }
    }

    /**
     * Resets the user's settings to their initial default state.
     *
     * This is achieved by overwriting the current persistence layer with a
     * default instance of [SettingsModel].
     *
     * @return A [Result] indicating whether the reset was successful.
     */
    override suspend fun resetAllSettings(): Result<Int> = wrap {
        AppLog.i(TAG, "resetAllSettings")
        dataSource.updateSettings(SettingsModel().toEntity())
    }

    companion object {
        private const val TAG = "SettingsMenuRepositoryImpl"
    }
}
