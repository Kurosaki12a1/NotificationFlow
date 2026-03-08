package com.kuro.notiflow.presentation.settings.ui.notification_filters.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.kuro.notiflow.presentation.common.R as CommonR
import com.kuro.notiflow.presentation.common.view.TopAppBarButton
import com.kuro.notiflow.presentation.common.vector.Restart
import com.kuro.notiflow.presentation.settings.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotificationFiltersTopAppBar(
    onBackClick: () -> Unit,
    onResetClick: () -> Unit
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
        actions = {
            TopAppBarButton(
                imageVector = Restart,
                imageDescription = stringResource(CommonR.string.resetToDefaultTitle),
                onButtonClick = onResetClick
            )
        },
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.notification_filters_title),
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
