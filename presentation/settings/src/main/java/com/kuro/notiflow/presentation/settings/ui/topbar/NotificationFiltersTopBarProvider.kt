package com.kuro.notiflow.presentation.settings.ui.topbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.notiflow.navigation.NavigationConstants.Destination.NOTIFICATION_FILTERS
import com.kuro.notiflow.presentation.common.AppScope
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.common.ui.local.LocalNavController
import com.kuro.notiflow.presentation.settings.ui.notification_filters.NotificationFiltersState
import com.kuro.notiflow.presentation.settings.ui.notification_filters.NotificationFiltersViewModel
import com.kuro.notiflow.presentation.settings.ui.notification_filters.components.NotificationFiltersTopAppBar
import jakarta.inject.Inject

class NotificationFiltersTopBarProvider @Inject constructor() : TopBarProvider {
    override val route: String = NOTIFICATION_FILTERS

    @Composable
    override fun AppScope.Render() {
        val navController = LocalNavController.current
        val backStackEntry by navController.currentBackStackEntryAsState()
        backStackEntry?.let {
            val viewModel: NotificationFiltersViewModel =
                hiltViewModel<NotificationFiltersViewModel>(it)
            val state by viewModel.state.collectAsStateWithLifecycle(NotificationFiltersState())
            NotificationFiltersTopAppBar(
                viewType = state.viewType,
                onBackClick = { popBackStack() },
                onViewTypeChange = viewModel::onViewTypeChanged
            )
        }
    }
}
