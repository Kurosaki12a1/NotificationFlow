package com.kuro.notiflow.presentation.settings.ui.data_management.components

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
import com.kuro.notiflow.presentation.settings.R
import com.kuro.notiflow.presentation.common.view.TopAppBarButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DataManagementTopAppBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            TopAppBarButton(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                imageDescription = stringResource(CommonR.string.back),
                onButtonClick = onBackClick,
            )
        },
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.dataManagementTitle),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        )
    )
}
