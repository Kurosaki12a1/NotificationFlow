package com.kuro.notiflow.presentation.bookmark.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kuro.notiflow.presentation.bookmark.R
import com.kuro.notiflow.presentation.bookmark.ui.components.BookmarkItem
import com.kuro.notiflow.presentation.common.R as CommonR

@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel = hiltViewModel()
) {
    val data = viewModel.bookmarkedNotifications.collectAsLazyPagingItems()
    val isEmpty = data.loadState.refresh is LoadState.NotLoading && data.itemCount == 0
    if (isEmpty) {
        EmptyState()
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(data.itemCount, key = { data[it]?.id ?: 0L }) { index ->
            val item = data[index]
            if (item != null) {
                BookmarkItem(
                    notification = item,
                    onBookmarkClick = { shouldBookmark ->
                        viewModel.setNotificationBookmark(item.id, shouldBookmark)
                    }
                )
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

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
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
