package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.data_source.datastore.AppDataStoreDataSource
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.api.datastore.AppDataRepository
import com.kuro.notiflow.domain.utils.AppLog
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppDataRepositoryImpl @Inject constructor(
    private val dataSource: AppDataStoreDataSource
) : AppDataRepository {
    private val firstLaunchKey = booleanPreferencesKey(Constants.DataStore.KEY_FIRST_LAUNCH)

    override val isFirstLaunch: Flow<Boolean> = dataSource.get(firstLaunchKey, true)

    override suspend fun setOnboardingCompleted() {
        AppLog.i(TAG, "setOnboardingCompleted")
        dataSource.set(firstLaunchKey, false)
    }

    companion object {
        private const val TAG = "AppDataRepositoryImpl"
    }
}
