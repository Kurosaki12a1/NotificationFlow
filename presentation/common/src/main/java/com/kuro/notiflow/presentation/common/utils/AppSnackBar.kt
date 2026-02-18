package com.kuro.notiflow.presentation.common.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable

object AppSnackBar {
    private var snackBar: SnackbarHostState? = null

    fun attachSnackBar(snackBar: SnackbarHostState) {
        this.snackBar = snackBar
    }

    fun deAttachSnackBar() {
        this.snackBar = null
    }

    suspend fun showSnackBar(message: String, withDismissAction: Boolean = true) {
        snackBar?.showSnackbar(message = message, withDismissAction = withDismissAction)
    }

    @Composable
    fun ErrorSnackBar() {
        snackBar?.let {
            SnackbarHost(
                hostState = it,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        actionColor = MaterialTheme.colorScheme.error,
                        dismissActionContentColor = MaterialTheme.colorScheme.onSurface,
                    )
                }
            )

        }
    }

}