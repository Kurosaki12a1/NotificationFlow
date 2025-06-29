package com.kuro.notiflow.presentation.common.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.presentation.R

@Composable
fun DialogButtons(
    modifier: Modifier = Modifier,
    enabledConfirm: Boolean = true,
    confirmTitle: String = stringResource(R.string.confirmTitle),
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    Row(
        modifier = modifier.padding(top = 16.dp, bottom = 16.dp, end = 16.dp, start = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onCancelClick) {
            Text(
                text = stringResource(R.string.cancelTitle),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
            )
        }
        TextButton(enabled = enabledConfirm, onClick = onConfirmClick) {
            Text(
                text = confirmTitle,
                color = when (enabledConfirm) {
                    true -> MaterialTheme.colorScheme.primary
                    false -> MaterialTheme.colorScheme.onSurfaceVariant
                },
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}