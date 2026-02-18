package com.kuro.notiflow.presentation.common.topbar

import androidx.compose.runtime.Composable

interface TopBarProvider {
    val route: String

    @Composable
    fun Render()
}