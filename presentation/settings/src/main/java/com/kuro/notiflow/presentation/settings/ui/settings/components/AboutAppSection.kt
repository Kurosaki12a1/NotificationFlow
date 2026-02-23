package com.kuro.notiflow.presentation.settings.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.logger.AppLog
import com.kuro.notiflow.presentation.common.R as CommonR
import com.kuro.notiflow.presentation.settings.R
import com.kuro.notiflow.presentation.common.extensions.scrollText

@Composable
internal fun AboutAppSection(
    modifier: Modifier = Modifier,
    onOpenGit: () -> Unit,
    onOpenIssues: () -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = stringResource(R.string.aboutAppHeader),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
        )
        AboutAppSectionVersion()
        AboutAppSectionDevelopment(
            onOpenGit = onOpenGit,
            onOpenIssues = onOpenIssues,
        )
    }
}

@Composable
internal fun AboutAppSectionVersion(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            InfoView(
                title = stringResource(R.string.versionNameTitle),
                text = "1.0", // TODO common BuildConfig.VersionCode
            )
            Spacer(modifier = Modifier.weight(1f))
            InfoView(
                title = stringResource(R.string.versionCodeTitle),
                text = "1.0",
            )
        }
    }
}

@Composable
internal fun AboutAppSectionDevelopment(
    modifier: Modifier = Modifier,
    onOpenGit: () -> Unit,
    onOpenIssues: () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                InfoView(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.developerTitle),
                    spaceInside = true,
                    text = Constants.App.DEVELOPER,
                )
                InfoView(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.licenseTitle),
                    spaceInside = true,
                    text = Constants.App.LICENCE,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    modifier = Modifier.weight(1f),
                    selected = true,
                    onClick = {
                        AppLog.i(TAG, "openIssues")
                        onOpenIssues()
                    },
                    label = {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .scrollText(),
                            text = stringResource(R.string.askQuestionTitle),
                            textAlign = TextAlign.Center,
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledSelectedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                            alpha = 0.6f
                        ),
                        selectedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                        selectedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                    )
                )
                FilterChip(
                    modifier = Modifier.weight(1f),
                    selected = true,
                    onClick = {
                        AppLog.i(TAG, "openGitHub")
                        onOpenGit()
                    },
                    label = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.githubTitle),
                            textAlign = TextAlign.Center,
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(CommonR.drawable.ic_github),
                            contentDescription = null,
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledSelectedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                            alpha = 0.6f
                        ),
                        selectedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                        selectedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                    )
                )
            }
        }
    }
}

private const val TAG = "AboutAppSection"

@Composable
internal fun InfoView(
    modifier: Modifier = Modifier,
    spaceInside: Boolean = false,
    title: String,
    text: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            style = MaterialTheme.typography.titleMedium,
        )
        if (spaceInside) Spacer(modifier = Modifier.weight(1f))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
