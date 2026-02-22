package com.kuro.notiflow.presentation.common.utils

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

val ImportMimeTypes = arrayOf(
    "text/csv",
    "text/comma-separated-values",
    "application/vnd.ms-excel",
    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    "text/plain"
)

@Composable
fun rememberImportLauncher(
    onResult: (Uri?) -> Unit
): ManagedActivityResultLauncher<Array<String>, Uri?> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = onResult
    )
}
