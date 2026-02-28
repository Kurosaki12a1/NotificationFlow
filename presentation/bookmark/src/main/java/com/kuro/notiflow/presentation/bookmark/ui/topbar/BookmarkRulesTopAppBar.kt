package com.kuro.notiflow.presentation.bookmark.ui.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.presentation.bookmark.R
import com.kuro.notiflow.presentation.common.view.TopAppBarButton
import com.kuro.notiflow.presentation.common.R as CommonR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BookmarkRulesTopAppBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            TopAppBarButton(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                imageDescription = stringResource(CommonR.string.back),
                onButtonClick = onBackClick
            )
        },
        title = {
            val layoutDirection = LocalLayoutDirection.current
            val startPadding = if (layoutDirection == LayoutDirection.Ltr) 0.dp else 16.dp
            val endPadding = if (layoutDirection == LayoutDirection.Ltr) 16.dp else 0.dp
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = startPadding, end = endPadding)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.bookmark_rules_title),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}
