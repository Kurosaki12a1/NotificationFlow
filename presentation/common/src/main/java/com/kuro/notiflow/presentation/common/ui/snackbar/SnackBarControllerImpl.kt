package com.kuro.notiflow.presentation.common.ui.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kuro.notiflow.presentation.common.utils.SnackBarType

class SnackBarControllerImpl(
    override val hostState: SnackbarHostState = SnackbarHostState()
) : SnackBarController {
    override var type: SnackBarType by mutableStateOf(SnackBarType.SUCCESS)
        private set
    private var lastMessage: String? = null
    private var lastShownAtMs: Long = 0L

    override suspend fun show(message: String, type: SnackBarType) {
        val now = System.currentTimeMillis()
        val shouldSkip = lastMessage == message && (now - lastShownAtMs) < DEBOUNCE_WINDOW_MS
        if (shouldSkip) return
        this.type = type
        hostState.currentSnackbarData?.dismiss()
        lastMessage = message
        lastShownAtMs = now
        hostState.showSnackbar(message = message)
    }

    private companion object {
        private const val DEBOUNCE_WINDOW_MS = 1500L
    }
}
