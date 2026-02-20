package com.kuro.notiflow.presentation.common.ui.local

import androidx.compose.runtime.staticCompositionLocalOf
import com.kuro.notiflow.presentation.common.ui.dialog.DialogController

val LocalDialogController = staticCompositionLocalOf<DialogController> {
    error("DialogController not provided")
}
