package com.kuro.notiflow.presentation.common.ui.local

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackBarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No SnackBarHostState provided")
}