package com.kuro.notiflow.presentation.common.ui.local

import androidx.compose.runtime.staticCompositionLocalOf
import com.kuro.notiflow.presentation.common.ui.snackbar.SnackBarController

val LocalSnackBarController = staticCompositionLocalOf<SnackBarController> {
    error("No SnackBarController provided")
}
