package com.kuro.notiflow.data.data_source.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.kuro.notiflow.domain.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppDataStoreDataSourceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AppDataStoreDataSource {
    private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(
        name = Constants.DataStore.NAME
    )

    override fun <T> get(key: Preferences.Key<T>, default: T): Flow<T> {
        return context.appDataStore.data.map { prefs ->
            prefs[key] ?: default
        }
    }

    override suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        context.appDataStore.edit { prefs ->
            prefs[key] = value
        }
    }

}
