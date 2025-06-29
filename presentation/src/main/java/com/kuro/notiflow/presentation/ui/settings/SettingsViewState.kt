package com.kuro.notiflow.presentation.ui.settings

import com.kuro.notiflow.domain.models.settings.ThemeSettings

data class SettingsViewState(
    val themeSettings: ThemeSettings? = null,
    val failure: String? = null
)