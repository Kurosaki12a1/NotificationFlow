package com.kuro.notiflow.presentation.settings.ui.settings

import com.kuro.notiflow.domain.models.settings.SettingsModel

data class SettingsViewState(
    val settingsModel: SettingsModel? = SettingsModel(),
    val failure: String? = null
)