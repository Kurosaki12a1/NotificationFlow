package com.kuro.notiflow.presentation.notifications.ui.filter.topbar

import androidx.compose.runtime.Composable
import com.kuro.notiflow.navigation.NavigationConstants.Destination.FILTER
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import javax.inject.Inject

class FilterTopBarProvider @Inject constructor() : TopBarProvider {
    override val route: String
        get() = FILTER

    @Composable
    override fun Render() {
        FilterTopAppBar()
    }
}