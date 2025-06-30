package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.settings.SettingsMenuRepository
import com.kuro.notiflow.domain.models.settings.SettingsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoadSettingsUseCase @Inject constructor(
    private val repository: SettingsMenuRepository
) {
    operator fun invoke() : Flow<SettingsModel> = flow {
        repository.fetchAllSettings()
    }
}