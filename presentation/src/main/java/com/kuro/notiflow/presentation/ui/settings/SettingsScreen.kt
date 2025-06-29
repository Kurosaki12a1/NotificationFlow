package com.kuro.notiflow.presentation.ui.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuro.notiflow.presentation.ui.settings.components.SettingsContent
import com.kuro.notiflow.presentation.ui.settings.components.SettingsTopAppBar

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val snackBarState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SettingsTopAppBar(
                onResetToDefaultClick = {

                }
            )
        }, content = { paddingValues ->
            SettingsContent(
                state = state,
                modifier = Modifier.padding(paddingValues),
                onUpdateThemeSettings = {
                    //     viewModel.dispatchEvent(SettingsEvent.ChangedThemeSettings(it))
                }
            )

        }, snackbarHost = {
            SnackbarHost(hostState = snackBarState)
        }
    )
}