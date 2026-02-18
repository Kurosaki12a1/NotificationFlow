package com.kuro.notiflow.presentation.home.ui.topbar

import androidx.compose.runtime.Composable
import com.kuro.notiflow.navigation.NavigationConstants.Destination.HOME
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import javax.inject.Inject

class HomeTopBarProvider @Inject constructor() : TopBarProvider {
    override val route: String
        get() = HOME

    @Composable
    override fun Render() {
        HomeTopAppBar()
    }
}