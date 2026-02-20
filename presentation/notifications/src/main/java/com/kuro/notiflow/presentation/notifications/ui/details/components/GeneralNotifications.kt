package com.kuro.notiflow.presentation.notifications.ui.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.presentation.common.R as CommonR
import com.kuro.notiflow.presentation.notifications.R
import com.kuro.notiflow.presentation.common.extensions.getAppName
import com.kuro.notiflow.presentation.common.extensions.scrollText
import com.kuro.notiflow.presentation.common.utils.Utils.convertMillisToTimeDetails
import com.kuro.notiflow.presentation.common.view.PackageIconImage
import com.kuro.notiflow.presentation.common.view.PriorityIcon
import com.kuro.notiflow.presentation.common.view.PriorityInfo
import com.kuro.notiflow.presentation.common.view.getPriorityColor

@Composable
fun GeneralNotifications(
    notification: NotificationModel?
) {
    if (notification == null) return
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PackageIconImage(
                    packageName = notification.packageName,
                    modifier = Modifier.size(48.dp)
                )
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .scrollText(),
                            text = notification.packageName.getAppName(context),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        PriorityIcon(level = notification.priority)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(CommonR.drawable.ic_clock),
                            contentDescription = "Clock",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = convertMillisToTimeDetails(notification.postTime),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Text(
                text = stringResource(R.string.description),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (!notification.title.isNullOrEmpty()) {
                Text(
                    text = notification.title!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!notification.text.isNullOrEmpty()) {
                Text(
                    text = notification.text!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!notification.bigText.isNullOrEmpty()) {
                Text(
                    text = notification.bigText!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!notification.subText.isNullOrEmpty()) {
                Text(
                    text = notification.subText!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            notification.textLines?.filter { it.isNotBlank() }?.takeIf { it.isNotEmpty() }
                ?.let { lines ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        lines.forEach { line ->
                            Row {
                                Text(
                                    "• $line",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            if (!notification.summaryText.isNullOrEmpty()) {
                Text(
                    text = notification.summaryText!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!notification.infoText.isNullOrEmpty()) {
                Text(
                    text = notification.infoText!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(CommonR.drawable.ic_tag),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = stringResource(R.string.category)
                    )
                    Text(
                        text = stringResource(R.string.category),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                color = getPriorityColor(notification.priority),
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = stringResource(R.string.level_priority),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = notification.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    PriorityInfo(notification.priority)
                }
            }
        }
    }
}
