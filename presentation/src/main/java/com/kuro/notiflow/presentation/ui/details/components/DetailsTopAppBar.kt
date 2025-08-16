package com.kuro.notiflow.presentation.ui.details.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.presentation.R
import com.kuro.notiflow.presentation.common.utils.AppNavigator
import com.kuro.notiflow.presentation.common.view.TopAppBarButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopAppBar(
    data: NotificationModel?,
    onBookmarkClicked: (Boolean) -> Unit
) {
    if (data == null) return
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            TopAppBarButton(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                imageDescription = stringResource(R.string.back),
                onButtonClick = {
                    AppNavigator.popBackStack()
                },
            )
        },
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.detailsTabTitle),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        },
        actions = {
            if (data.isBookmarked) {
                TopAppBarButton(
                    imagePainter = painterResource(R.drawable.ic_bookmark_remove),
                    imageDescription = stringResource(R.string.bookmark),
                    onButtonClick = { onBookmarkClicked(false) },
                )
            } else {
                TopAppBarButton(
                    imagePainter = painterResource(R.drawable.ic_bookmark_add),
                    imageDescription = stringResource(R.string.bookmark),
                    onButtonClick = { onBookmarkClicked(true) },
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        )
    )
}