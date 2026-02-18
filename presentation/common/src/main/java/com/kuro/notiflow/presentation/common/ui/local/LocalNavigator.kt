package com.kuro.notiflow.presentation.common.ui.local

import androidx.compose.runtime.staticCompositionLocalOf
import com.kuro.notiflow.navigation.utils.AppNavigator

val LocalNavigator = staticCompositionLocalOf<AppNavigator> {
    error("Navigator not provided")
}
