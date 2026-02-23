package com.kuro.notiflow.presentation.common.ui.snackbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable

@Composable
fun InfoSnackBar(data: SnackbarData) {
    Snackbar(
        snackbarData = data,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        actionColor = MaterialTheme.colorScheme.primary,
        dismissActionContentColor = MaterialTheme.colorScheme.onSurface
    )
}
