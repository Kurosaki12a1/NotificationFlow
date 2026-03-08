package com.kuro.notiflow.presentation.notifications.ui.filter.topbar

import androidx.compose.runtime.Composable
import com.kuro.notiflow.domain.utils.AppLog
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuro.notiflow.navigation.NavigationConstants.Destination.FILTER
import com.kuro.notiflow.presentation.common.AppScope
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.common.ui.local.LocalNavController
import com.kuro.notiflow.presentation.notifications.ui.main.NotificationsViewModel
import javax.inject.Inject

class FilterTopBarProvider @Inject constructor() : TopBarProvider {
    override val route: String = FILTER

    @Composable
    override fun AppScope.Render() {
        val navController = LocalNavController.current
        val owner = navController.previousBackStackEntry
        val viewModel = owner?.let { entry ->
            hiltViewModel<NotificationsViewModel>(entry)
        }
        FilterTopAppBar(
            onBackClick = {
                AppLog.d(TAG, "back")
                popBackStack()
            },
            onResetClick = {
                AppLog.d(TAG, "reset")
                viewModel?.resetListFilters()
            }
        )
    }
}

private const val TAG = "FilterTopBarProvider"
