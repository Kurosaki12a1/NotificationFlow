package com.kuro.notiflow.presentation.home.ui.topbar

import androidx.compose.runtime.Composable
import com.kuro.notiflow.navigation.NavigationConstants.Destination.HOME
import com.kuro.notiflow.presentation.common.AppScope
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import javax.inject.Inject

class HomeTopBarProvider @Inject constructor() : TopBarProvider {
    override val route: String = HOME

    @Composable
    override fun AppScope.Render() {
        HomeTopAppBar()
    }
}
