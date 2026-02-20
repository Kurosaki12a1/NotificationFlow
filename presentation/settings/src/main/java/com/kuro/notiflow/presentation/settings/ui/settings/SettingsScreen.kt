package com.kuro.notiflow.presentation.settings.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.presentation.common.ui.local.LocalNavigator
import com.kuro.notiflow.presentation.settings.ui.settings.components.SettingsContent

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current

    SettingsContent(
        state = state,
        modifier = Modifier,
        onUpdateSettings = {
            viewModel.updateSettings(it)
        },
        onDataManagementClick = {
            navigator.navigateTo(Screen.DataManagement)
        }
    )
}
