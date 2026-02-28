package com.kuro.notiflow.presentation.common.topbar

import androidx.compose.runtime.Composable
import com.kuro.notiflow.presentation.common.AppScope

interface TopBarProvider {
    val route: String

    @Composable
    fun AppScope.Render()
}
