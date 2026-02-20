package com.kuro.notiflow.presentation.common.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuro.notiflow.presentation.common.ui.local.LocalDialogController

/**
 * A dialog specification that knows how to render itself. Feature modules can provide their own
 * implementations to keep UI-specific dialogs local to that feature.
 */
interface AppDialogSpec {
    @Composable
    fun Render(controller: DialogController)
}

@Composable
fun AppDialogHost() {
    val dialogController = LocalDialogController.current
    val spec by dialogController.dialog.collectAsStateWithLifecycle()
    spec?.Render(dialogController)
}
