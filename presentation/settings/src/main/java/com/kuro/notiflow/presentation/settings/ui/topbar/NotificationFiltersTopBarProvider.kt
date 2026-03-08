package com.kuro.notiflow.presentation.settings.ui.topbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.notiflow.navigation.NavigationConstants.Destination.NOTIFICATION_FILTERS
import com.kuro.notiflow.presentation.common.AppScope
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.common.ui.dialog.ConfirmDialogSpec
import com.kuro.notiflow.presentation.common.ui.local.LocalNavController
import com.kuro.notiflow.presentation.settings.ui.notification_filters.NotificationFiltersViewModel
import com.kuro.notiflow.presentation.settings.ui.notification_filters.components.NotificationFiltersTopAppBar
import com.kuro.notiflow.presentation.settings.R
import jakarta.inject.Inject
import com.kuro.notiflow.presentation.common.R as CommonR

class NotificationFiltersTopBarProvider @Inject constructor() : TopBarProvider {
    override val route: String = NOTIFICATION_FILTERS

    @Composable
    override fun AppScope.Render() {
        val navController = LocalNavController.current
        val backStackEntry by navController.currentBackStackEntryAsState()
        backStackEntry?.let {
            val viewModel: NotificationFiltersViewModel =
                hiltViewModel<NotificationFiltersViewModel>(it)
            NotificationFiltersTopAppBar(
                onBackClick = viewModel::onBackRequested,
                onResetClick = {
                    showDialog(
                        ConfirmDialogSpec(
                            title = string(R.string.notification_filters_reset_dialog_title),
                            message = string(R.string.notification_filters_reset_dialog_message),
                            confirmText = string(R.string.notification_filters_reset_dialog_confirm),
                            cancelText = string(CommonR.string.cancelTitle),
                            onConfirm = viewModel::resetSelection
                        )
                    )
                }
            )
        }
    }
}
