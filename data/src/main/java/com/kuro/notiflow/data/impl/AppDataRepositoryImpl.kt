package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.data_source.datastore.AppDataStoreDataSource
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.api.datastore.AppDataRepository
import com.kuro.notiflow.domain.models.notifications.NotificationFilterMode
import com.kuro.notiflow.domain.models.notifications.NotificationFilterSettings
import com.kuro.notiflow.domain.utils.AppLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class AppDataRepositoryImpl @Inject constructor(
    private val dataSource: AppDataStoreDataSource
) : AppDataRepository {
    companion object {
        private const val TAG = "AppDataRepositoryImpl"
    }

    private val firstLaunchKey = booleanPreferencesKey(Constants.DataStore.KEY_FIRST_LAUNCH)
    private val notificationFilterModeKey =
        stringPreferencesKey(Constants.DataStore.KEY_NOTIFICATION_FILTER_MODE)
    private val notificationFilterPackagesKey =
        stringSetPreferencesKey(Constants.DataStore.KEY_NOTIFICATION_FILTER_PACKAGES)

    override val isFirstLaunch: Flow<Boolean> = dataSource.get(firstLaunchKey, true)

    override val notificationFilterSettings: Flow<NotificationFilterSettings> = combine(
        dataSource.get(
            notificationFilterModeKey,
            NotificationFilterMode.ALLOW_ALL.name
        ),
        dataSource.get(notificationFilterPackagesKey, emptySet())
    ) { modeName, packages ->
        NotificationFilterSettings(
            mode = modeName.toNotificationFilterMode(),
            packageNames = packages
        )
    }

    override suspend fun setOnboardingCompleted() {
        AppLog.i(TAG, "setOnboardingCompleted")
        dataSource.set(firstLaunchKey, false)
    }

    override suspend fun updateNotificationFilterSettings(settings: NotificationFilterSettings) {
        AppLog.i(
            TAG,
            "updateNotificationFilterSettings mode=${settings.mode} size=${settings.packageNames.size}"
        )
        dataSource.set(notificationFilterModeKey, settings.mode.name)
        dataSource.set(notificationFilterPackagesKey, settings.packageNames)
    }

    private fun String.toNotificationFilterMode(): NotificationFilterMode {
        return runCatching { NotificationFilterMode.valueOf(this) }
            .getOrDefault(NotificationFilterMode.ALLOW_ALL)
    }

}
