package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.datastore.AppDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnboardingUseCase @Inject constructor(
    private val repository: AppDataRepository
) {
    val isFirstLaunch: Flow<Boolean> = repository.isFirstLaunch

    suspend fun completeOnboarding() {
        repository.setOnboardingCompleted()
    }
}
