package com.kuro.notiflow.domain.api.datastore

import kotlinx.coroutines.flow.Flow

interface AppDataRepository {
    val isFirstLaunch: Flow<Boolean>
    suspend fun setOnboardingCompleted()
}
