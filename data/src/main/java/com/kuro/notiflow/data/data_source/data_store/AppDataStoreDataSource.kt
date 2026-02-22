package com.kuro.notiflow.data.data_source.data_store

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface AppDataStoreDataSource {
    fun <T> get(
        key: Preferences.Key<T>,
        default: T
    ): Flow<T>

    suspend fun <T> set(
        key: Preferences.Key<T>,
        value: T
    )
}
