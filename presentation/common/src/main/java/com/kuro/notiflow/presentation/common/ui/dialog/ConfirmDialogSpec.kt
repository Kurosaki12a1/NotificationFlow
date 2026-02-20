package com.kuro.notiflow.presentation.common.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * Standard confirm dialog with title, message and two actions.
 */
data class ConfirmDialogSpec(
    val title: String,
    val message: String,
    val confirmText: String,
    val cancelText: String,
    val onConfirm: () -> Unit,
    val onDismiss: (() -> Unit)? = null,
) : AppDialogSpec {
    @Composable
    override fun Render(controller: DialogController) {
        Dialog(
            onDismissRequest = {
                onDismiss?.invoke()
                controller.hide()
            }
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(
                            onClick = {
                                onDismiss?.invoke()
                                controller.hide()
                            }
                        ) {
                            Text(text = cancelText)
                        }
                        TextButton(
                            onClick = {
                                onConfirm()
                                controller.hide()
                            }
                        ) {
                            Text(text = confirmText)
                        }
                    }
                }
            }
        }
    }
}
