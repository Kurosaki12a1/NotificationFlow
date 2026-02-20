package com.kuro.notiflow.presentation.common.ui.dialog

import kotlinx.coroutines.flow.StateFlow

/**
 * Dialog controller used by UI to display or hide dialogs.
 */
interface DialogController {
    val dialog: StateFlow<AppDialogSpec?>

    fun show(spec: AppDialogSpec)
    fun hide()

    fun showLoading(message: String? = null, dismissible: Boolean = false)
    fun hideIfLoading()
}
