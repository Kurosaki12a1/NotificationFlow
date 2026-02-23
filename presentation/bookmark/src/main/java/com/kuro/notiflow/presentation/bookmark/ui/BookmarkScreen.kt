package com.kuro.notiflow.presentation.bookmark.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.presentation.bookmark.R
import com.kuro.notiflow.domain.logger.AppLog
import com.kuro.notiflow.presentation.common.extensions.getAppName
import com.kuro.notiflow.presentation.common.extensions.scrollText
import com.kuro.notiflow.presentation.common.ui.local.LocalNavigator
import com.kuro.notiflow.presentation.common.utils.Utils.convertMillisToTime
import com.kuro.notiflow.presentation.common.view.PackageIconImage
import com.kuro.notiflow.presentation.common.R as CommonR

@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel = hiltViewModel()
) {
    val data = viewModel.bookmarkedNotifications.collectAsLazyPagingItems()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (data.itemCount == 0) {
            EmptyState()
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(data.itemCount, key = { data[it]?.id ?: 0L }) { index ->
                val item = data[index]
                if (item != null) {
                    BookmarkItem(notification = item, isEven = index % 2 == 0)
                } else {
                    Text(
                        text = stringResource(CommonR.string.loading),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        androidx.compose.material3.Icon(
            painter = painterResource(CommonR.drawable.ic_bookmark_outline),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = stringResource(R.string.bookmark_empty_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = stringResource(R.string.bookmark_empty_subtitle),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun BookmarkItem(
    notification: NotificationModel,
    isEven: Boolean
) {
    val context = LocalContext.current
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isEven) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.medium
            )
            .clickable {
                AppLog.d(
                    TAG,
                    "openDetail id=${notification.id} pkg=${notification.packageName}"
                )
                navigator.navigateTo(Screen.NotificationDetail(notification.id))
            }
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PackageIconImage(
                packageName = notification.packageName,
                modifier = Modifier.size(24.dp)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .scrollText(),
                text = notification.packageName.getAppName(context),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = convertMillisToTime(notification.postTime),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (!notification.title.isNullOrBlank()) {
            Text(
                text = notification.title ?: "",
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private const val TAG = "BookmarkScreen"
