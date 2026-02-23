package com.kuro.notiflow.presentation.common.ui.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.kuro.notiflow.presentation.common.utils.SnackBarType

interface SnackBarController {
    val hostState: SnackbarHostState
    val type: SnackBarType

    suspend fun show(
        message: String,
        type: SnackBarType = SnackBarType.SUCCESS
    )

    suspend fun showAction(
        message: String,
        actionLabel: String,
        type: SnackBarType = SnackBarType.SUCCESS
    ): SnackbarResult
}
