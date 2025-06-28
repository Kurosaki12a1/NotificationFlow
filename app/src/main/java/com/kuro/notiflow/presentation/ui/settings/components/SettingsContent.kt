package com.kuro.notiflow.presentation.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.R
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.models.settings.ThemeSettings
import com.kuro.notiflow.presentation.common.theme.ColorType
import com.kuro.notiflow.presentation.common.theme.LanguageType
import com.kuro.notiflow.presentation.common.theme.ThemeType
import com.kuro.notiflow.presentation.ui.settings.SettingsViewState

@Composable
internal fun SettingsContent(
    state: SettingsViewState,
    modifier: Modifier = Modifier,
    onUpdateThemeSettings: (ThemeSettings) -> Unit
) {
    if (state.themeSettings != null) {
        val uriHandler = LocalUriHandler.current
        val scrollState = rememberLazyListState()
        LazyColumn(
            state = scrollState,
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    MainSettingsSection(
                        languageType = state.themeSettings.language,
                        themeColors = state.themeSettings.themeColors,
                        colorsType = state.themeSettings.colorsType,
                        dynamicColor = state.themeSettings.isDynamicColorEnable,
                        onThemeColorUpdate = { colorsType ->
                            onUpdateThemeSettings(state.themeSettings.copy(themeColors = colorsType))
                        },
                        onLanguageChange = { language ->
                            onUpdateThemeSettings(state.themeSettings.copy(language = language))
                        },
                        onColorsTypeUpdate = { colorsType ->
                            onUpdateThemeSettings(state.themeSettings.copy(colorsType = colorsType))
                        },
                        onDynamicColorsChange = {
                            onUpdateThemeSettings(state.themeSettings.copy(isDynamicColorEnable = it))
                        },
                    )
                    HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
                }
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AboutAppSection(
                        onOpenGit = {
                            uriHandler.openUri(Constants.App.GITHUB_URI)
                        },
                        onOpenIssues = {
                            uriHandler.openUri(Constants.App.ISSUES_URI)
                        },
                    )
                }
            }
        }
    }
}

@Composable
internal fun MainSettingsSection(
    modifier: Modifier = Modifier,
    languageType: LanguageType,
    themeColors: ThemeType,
    colorsType: ColorType,
    dynamicColor: Boolean,
    onLanguageChange: (LanguageType) -> Unit,
    onThemeColorUpdate: (ThemeType) -> Unit,
    onColorsTypeUpdate: (ColorType) -> Unit,
    onDynamicColorsChange: (Boolean) -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = stringResource(R.string.mainSettingsTitle),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
        )
        ThemeColorsChooser(
            modifier = Modifier.fillMaxWidth(),
            themeColors = themeColors,
            onThemeColorUpdate = onThemeColorUpdate,
        )
        ColorsTypeChooser(
            colorsType = colorsType,
            onChoose = onColorsTypeUpdate,
        )
        DynamicColorChooser(
            dynamicColor = dynamicColor,
            onChange = onDynamicColorsChange,
        )
        LanguageChooser(
            language = languageType,
            onLanguageChanged = onLanguageChange,
        )
    }
}