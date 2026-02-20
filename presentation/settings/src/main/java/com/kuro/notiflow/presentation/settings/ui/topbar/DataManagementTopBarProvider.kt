package com.kuro.notiflow.presentation.settings.ui.topbar

import androidx.compose.runtime.Composable
import com.kuro.notiflow.navigation.NavigationConstants.Destination.DATA_MANAGEMENT
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.settings.ui.data_management.components.DataManagementTopAppBar
import jakarta.inject.Inject

class DataManagementTopBarProvider @Inject constructor() : TopBarProvider {
    override val route: String
        get() = DATA_MANAGEMENT

    @Composable
    override fun Render() {
        DataManagementTopAppBar()
    }
}