package com.kuro.notiflow.presentation.bookmark.ui.rules.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.domain.models.bookmark.BookmarkRule
import com.kuro.notiflow.presentation.bookmark.R
import com.kuro.notiflow.presentation.common.view.PackageIconImage

@Composable
internal fun BookmarkRuleItem(
    appName: String?,
    packageName: String?,
    rule: BookmarkRule,
    onEditClick: () -> Unit,
    onEnabledChanged: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!packageName.isNullOrBlank()) {
                    PackageIconImage(
                        packageName = packageName,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    modifier = Modifier.weight(1f),
                    text = appName ?: rule.packageName
                        ?: stringResource(R.string.bookmark_rules_all_apps),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Switch(
                    checked = rule.isEnabled,
                    onCheckedChange = onEnabledChanged
                )
            }
            val summaryText = if (rule.keyword.isBlank()) {
                stringResource(
                    R.string.bookmark_rules_summary_any_words,
                    bookmarkRuleMatchFieldLabel(rule.matchField),
                    bookmarkRuleMatchTypeLabel(rule.matchType)
                )
            } else {
                stringResource(
                    R.string.bookmark_rules_summary,
                    bookmarkRuleMatchFieldLabel(rule.matchField),
                    bookmarkRuleMatchTypeLabel(rule.matchType),
                    rule.keyword
                )
            }
            Text(
                text = summaryText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (!packageName.isNullOrBlank()) {
                Text(
                    text = packageName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onEditClick
                ) {
                    Text(text = stringResource(R.string.bookmark_rules_edit))
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onDeleteClick
                ) {
                    Text(text = stringResource(R.string.bookmark_rules_delete))
                }
            }
        }
    }
}
