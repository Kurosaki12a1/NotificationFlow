package com.kuro.notiflow.presentation.settings.ui.settings.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.kuro.notiflow.presentation.common.R
import com.kuro.notiflow.presentation.common.view.TopAppBarAction
import com.kuro.notiflow.presentation.common.view.TopAppBarMoreActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsTopAppBar(
    onResetToDefaultClick: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.settingsTabTitle),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                )
                TopAppBarMoreActions(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    items = SettingsMoreActions.entries.toTypedArray(),
                    moreIconDescription = stringResource(R.string.moreIconDesc),
                    onItemClick = { action ->
                        when (action) {
                            SettingsMoreActions.RESET_TO_DEFAULT -> onResetToDefaultClick.invoke()
                        }
                    },
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        )
    )
}

internal enum class SettingsMoreActions : TopAppBarAction {
    RESET_TO_DEFAULT {
        override val title: String @Composable get() = stringResource(R.string.resetToDefaultTitle)
        override val icon: Painter @Composable get() = painterResource(R.drawable.ic_reset)
        override val isAlwaysShow: Boolean = false
    }
}