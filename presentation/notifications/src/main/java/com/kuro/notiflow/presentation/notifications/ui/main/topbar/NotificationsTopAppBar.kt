package com.kuro.notiflow.presentation.notifications.ui.main.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.presentation.common.extensions.scrollText
import com.kuro.notiflow.presentation.common.view.CustomLargeTextField
import com.kuro.notiflow.presentation.notifications.R
import com.kuro.notiflow.presentation.common.R as CommonR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsTopAppBar(
    totalNotifications: Int,
    onFilterClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            val layoutDirection = LocalLayoutDirection.current
            val startPadding = if (layoutDirection == LayoutDirection.Ltr) 0.dp else 16.dp
            val endPadding = if (layoutDirection == LayoutDirection.Ltr) 16.dp else 0.dp
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = startPadding, end = endPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        painter = painterResource(CommonR.drawable.ic_notifications),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.notifications_title),
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )
                    if (totalNotifications > 0) {
                        Box(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(
                                    CommonR.string.notifications_count,
                                    totalNotifications
                                ),
                                textAlign = TextAlign.End,
                                modifier = Modifier.scrollText(),
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
                CustomLargeTextField(
                    text = "",
                    onTextChange = { },
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
                            modifier = Modifier.clickable { onFilterClick() },
                            painter = painterResource(CommonR.drawable.ic_filter),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    background = MaterialTheme.colorScheme.background
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        windowInsets = WindowInsets.safeContent.only(WindowInsetsSides.Top),
        scrollBehavior = scrollBehavior
    )
}
