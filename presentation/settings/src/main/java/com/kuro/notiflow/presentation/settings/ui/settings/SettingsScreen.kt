package com.kuro.notiflow.presentation.settings.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuro.notiflow.presentation.settings.ui.settings.components.SettingsContent

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state

    SettingsContent(
        state = state,
        modifier = Modifier,
        onUpdateSettings = {
            viewModel.updateSettings(it)
        }
    )
}