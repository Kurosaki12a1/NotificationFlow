package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.settings.SettingsMenuRepository
import com.kuro.notiflow.domain.models.settings.SettingsModel
import javax.inject.Inject

class UpdateSettingsUseCase @Inject constructor(
    private val repository: SettingsMenuRepository
) {
    suspend operator fun invoke(settingsModel: SettingsModel) =
        repository.updateSettings(settingsModel)
}