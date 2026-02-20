package com.kuro.notiflow.presentation.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.domain.models.notifications.NotificationStats
import com.kuro.notiflow.presentation.home.R
import com.kuro.notiflow.presentation.common.theme.LocalAppColors
import com.kuro.notiflow.presentation.common.vector.Clock
import com.kuro.notiflow.presentation.common.vector.Statistic
import com.kuro.notiflow.presentation.common.vector.UnReadNotifications

@Composable
fun OverviewSection(
    overViewStats: NotificationStats
) {
    val appColors = LocalAppColors.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OverviewItem(
                    title = stringResource(R.string.total_notifications),
                    subTitle = "${overViewStats.totalCount}",
                    background = appColors.color1,
                    icon = Icons.Default.Notifications
                )
                OverviewItem(
                    title = stringResource(R.string.unread),
                    subTitle = "${overViewStats.unreadCount}",
                    background = appColors.color2,
                    icon = UnReadNotifications
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OverviewItem(
                    title = stringResource(R.string.today),
                    subTitle = "${overViewStats.todayCount}",
                    background = appColors.color3,
                    icon = Clock
                )
                OverviewItem(
                    title = stringResource(R.string.growth),
                    subTitle = overViewStats.getNotificationGrowthThisWeekVsLastWeek(),
                    background = appColors.color4,
                    icon = Statistic
                )
            }
        }
    }
}

@Composable
private fun RowScope.OverviewItem(
    title: String,
    subTitle: String,
    background: Color,
    icon: ImageVector
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .background(background, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = subTitle,
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = 6.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp),
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.background
            )
        }
    }

}
