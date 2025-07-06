package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.settings.SettingsMenuRepository
import javax.inject.Inject

class ResetSettingsUseCase @Inject constructor(
    private val repository: SettingsMenuRepository
) {
    suspend operator fun invoke() = repository.resetAllSettings()
}