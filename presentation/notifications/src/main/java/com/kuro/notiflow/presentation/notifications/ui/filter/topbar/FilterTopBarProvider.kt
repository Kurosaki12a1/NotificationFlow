package com.kuro.notiflow.presentation.notifications.ui.filter.topbar

import androidx.compose.runtime.Composable
import com.kuro.notiflow.domain.utils.AppLog
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuro.notiflow.navigation.NavigationConstants.Destination.FILTER
import com.kuro.notiflow.presentation.common.AppScope
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.notifications.ui.filter.FilterViewModel
import javax.inject.Inject

class FilterTopBarProvider @Inject constructor() : TopBarProvider {
    override val route: String = FILTER

    @Composable
    override fun AppScope.Render() {
        val viewModel: FilterViewModel = hiltViewModel()
        FilterTopAppBar(
            onBackClick = {
                AppLog.d(TAG, "back")
                popBackStack()
            },
            onResetClick = {
                AppLog.d(TAG, "reset")
                viewModel.resetToDefaults()
            }
        )
    }
}

private const val TAG = "FilterTopBarProvider"
