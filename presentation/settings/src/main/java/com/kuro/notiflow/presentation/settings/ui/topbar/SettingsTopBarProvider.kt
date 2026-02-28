package com.kuro.notiflow.presentation.settings.ui.topbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.notiflow.navigation.NavigationConstants.Destination.SETTINGS
import com.kuro.notiflow.presentation.common.AppScope
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.common.ui.local.LocalNavController
import com.kuro.notiflow.presentation.settings.ui.settings.SettingsViewModel
import com.kuro.notiflow.presentation.settings.ui.settings.components.SettingsTopAppBar
import javax.inject.Inject

class SettingsTopBarProvider @Inject constructor() : TopBarProvider {
    override val route: String
        get() = SETTINGS

    @Composable
    override fun AppScope.Render() {
        val navController = LocalNavController.current
        val backStackEntry by navController.currentBackStackEntryAsState()
        backStackEntry?.let {
            val viewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>(it)
            SettingsTopAppBar(
                onResetToDefaultClick = {
                    viewModel.resetToDefault()
                }
            )
        }
    }
}
