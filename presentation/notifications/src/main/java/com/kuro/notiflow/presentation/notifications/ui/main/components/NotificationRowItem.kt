package com.kuro.notiflow.presentation.notifications.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.presentation.common.extensions.getAppName
import com.kuro.notiflow.presentation.common.extensions.scrollText
import com.kuro.notiflow.presentation.common.utils.Utils.convertMillisToTime
import com.kuro.notiflow.presentation.common.view.PackageIconImage
import com.kuro.notiflow.presentation.notifications.R
import com.kuro.notiflow.presentation.common.R as CommonR

@Composable
fun NotificationRowItem(
    notification: NotificationModel,
    onClick: () -> Unit,
    onBookmarkClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PackageIconImage(
            packageName = notification.packageName,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .scrollText(),
                    text = notification.packageName.getAppName(context),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = convertMillisToTime(notification.postTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (!notification.isRead) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    )
                }
            }
            if (!notification.title.isNullOrBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = notification.title ?: "",
                        maxLines = 1,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    IconButton(
                        onClick = { onBookmarkClick(!notification.isBookmarked) }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (notification.isBookmarked) {
                                    CommonR.drawable.ic_bookmark
                                } else {
                                    CommonR.drawable.ic_bookmark_outline
                                }
                            ),
                            contentDescription = stringResource(R.string.bookmark),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
