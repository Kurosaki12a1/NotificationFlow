package com.kuro.notiflow.presentation.notifications.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
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
import com.kuro.notiflow.presentation.common.R
import com.kuro.notiflow.presentation.common.extensions.getAppName
import com.kuro.notiflow.presentation.common.extensions.scrollText
import com.kuro.notiflow.presentation.common.navigation.LocalNavigator
import com.kuro.notiflow.presentation.common.utils.Utils.convertMillisToTime
import com.kuro.notiflow.presentation.common.view.CustomLargeTextField
import com.kuro.notiflow.presentation.common.view.PackageIconImage

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val data = viewModel.listNotifications.collectAsLazyPagingItems()
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomLargeTextField(
            text = "",
            onTextChange = {

            },
            label = { },
            placeholder = { Text(text = stringResource(R.string.search_hint)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            maxLines = 1,
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        navigator.navigateTo(Screen.Filter)
                    },
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            background = MaterialTheme.colorScheme.background
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(data.itemCount, key = { data[it]?.id ?: 0L }) { index ->
                val item = data[index]
                if (item != null) {
                    ItemLogNotifications(notification = item, isEven = index % 2 == 0)
                } else {
                    Text(
                        text = stringResource(R.string.loading),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemLogNotifications(
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
                RoundedCornerShape(16.dp)
            )
            .clickable {
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
            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(MaterialTheme.colorScheme.surfaceTint, CircleShape)
                )
            }
        }
        if (notification.title != "") {
            Text(
                text = notification.title ?: "",
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}