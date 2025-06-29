package com.kuro.notiflow.presentation.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.presentation.R
import com.kuro.notiflow.domain.models.settings.ThemeType
import com.kuro.notiflow.presentation.common.view.SegmentedButtonItem
import com.kuro.notiflow.presentation.common.view.SegmentedButtons

@Composable
internal fun ThemeColorsChooser(
    modifier: Modifier = Modifier,
    themeColors: ThemeType,
    onThemeColorUpdate: ThemeType.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.mainSettingsThemeTitle),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )
            SegmentedButtons(
                modifier = Modifier.fillMaxWidth(),
                items = ThemeColorsTypeSegmentedItems.entries.toTypedArray(),
                selectedItem = themeColors.toSegmentedItem(),
                onItemClick = { onThemeColorUpdate.invoke(it.toThemeColorsType()) },
            )
        }
    }
}

internal enum class ThemeColorsTypeSegmentedItems : SegmentedButtonItem {
    LIGHT {
        override val title: String @Composable get() = stringResource(R.string.lightThemeTitle)
    },
    DEFAULT {
        override val title: String @Composable get() = stringResource(R.string.systemThemeTitle)
    },
    DARK {
        override val title: String @Composable get() = stringResource(R.string.darkThemeTitle)
    },
}

internal fun ThemeType.toSegmentedItem(): ThemeColorsTypeSegmentedItems = when (this) {
    ThemeType.DEFAULT -> ThemeColorsTypeSegmentedItems.DEFAULT
    ThemeType.LIGHT -> ThemeColorsTypeSegmentedItems.LIGHT
    ThemeType.DARK -> ThemeColorsTypeSegmentedItems.DARK
}

internal fun ThemeColorsTypeSegmentedItems.toThemeColorsType(): ThemeType = when (this) {
    ThemeColorsTypeSegmentedItems.LIGHT -> ThemeType.LIGHT
    ThemeColorsTypeSegmentedItems.DEFAULT -> ThemeType.DEFAULT
    ThemeColorsTypeSegmentedItems.DARK -> ThemeType.DARK
}
