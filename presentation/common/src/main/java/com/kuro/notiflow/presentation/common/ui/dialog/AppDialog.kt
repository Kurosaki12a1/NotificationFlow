package com.kuro.notiflow.presentation.common.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

/**
 * A dialog specification that knows how to render itself. Feature modules can provide their own
 * implementations to keep UI-specific dialogs local to that feature.
 */
interface AppDialogSpec {
    @Composable
    fun Render(controller: DialogController)
}

@Composable
fun AppDialogHost(
    dialogController: DialogController
) {
    val spec by dialogController.dialog.collectAsState()
    spec?.Render(dialogController)
}
