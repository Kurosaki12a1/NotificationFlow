package com.kuro.notiflow.presentation.common.ui.snackbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable

@Composable
fun ErrorSnackBar(data: SnackbarData) {
    Snackbar(
        snackbarData = data,
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        actionColor = MaterialTheme.colorScheme.error,
        dismissActionContentColor = MaterialTheme.colorScheme.onSurface
    )
}
