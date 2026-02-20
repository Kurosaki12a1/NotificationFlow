package com.kuro.notiflow.presentation.common.ui.dialog

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


/**
 * Singleton dialog controller backed by StateFlow so the dialog survives configuration changes.
 */
class DialogControllerImpl : DialogController {
    private val _dialog = MutableStateFlow<AppDialogSpec?>(null)

    override val dialog: StateFlow<AppDialogSpec?>
        get() = _dialog.asStateFlow()

    override fun show(spec: AppDialogSpec) {
        // If the same spec is shown consecutively, clear first to force a new emission.
        if (_dialog.value == spec) {
            _dialog.value = null
        }
        _dialog.value = spec
    }

    override fun hide() {
        // Clear any active dialog.
        _dialog.value = null
    }

    override fun showLoading(message: String?, dismissible: Boolean) {
        // Convenience wrapper for a standard loading dialog.
        show(LoadingDialogSpec(message = message, dismissible = dismissible))
    }

    override fun hideIfLoading() {
        // Avoid closing a non-loading dialog by mistake.
        if (_dialog.value is LoadingDialogSpec) {
            hide()
        }
    }
}
