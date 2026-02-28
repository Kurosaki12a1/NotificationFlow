package com.kuro.notiflow.presentation.bookmark.ui.rules.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.presentation.bookmark.R
import com.kuro.notiflow.presentation.common.ui.dialog.SelectionDialog
import com.kuro.notiflow.presentation.common.view.PackageIconImage

@Composable
internal fun AppSelectionDialog(
    availableApps: List<AppSelectionItem>,
    selectedPackageName: String?,
    onDismiss: () -> Unit,
    onSelectApp: (AppSelectionItem?) -> Unit
) {
    SelectionDialog(
        title = stringResource(R.string.bookmark_rules_choose_app),
        onDismiss = onDismiss
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 320.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(key = ALL_APPS_ITEM_KEY) {
                SelectionRow(
                    title = stringResource(R.string.bookmark_rules_all_apps),
                    subtitle = stringResource(R.string.bookmark_rules_all_apps_subtitle),
                    isSelected = selectedPackageName == null,
                    onClick = { onSelectApp(null) }
                )
            }
            if (availableApps.isEmpty()) {
                item(key = EMPTY_HINT_ITEM_KEY) {
                    Text(
                        text = stringResource(R.string.bookmark_rules_no_apps_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(availableApps, key = { it.packageName }) { app ->
                    SelectionRow(
                        title = app.appName,
                        subtitle = app.packageName,
                        isSelected = app.packageName == selectedPackageName,
                        leading = {
                            PackageIconImage(
                                packageName = app.packageName,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        onClick = { onSelectApp(app) }
                    )
                }
            }
        }
    }
}

private const val ALL_APPS_ITEM_KEY = "all_apps"
private const val EMPTY_HINT_ITEM_KEY = "empty_hint"

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun SelectionRow(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    leading: (@Composable () -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerLow
        },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            leading?.invoke()
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.bookmark_rules_selected),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
