package com.kuro.notiflow.presentation.ui.main

import com.kuro.notiflow.domain.models.settings.SettingsModel

data class MainViewState(
    val settingsModel: SettingsModel = SettingsModel(),
)