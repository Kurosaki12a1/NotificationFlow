package com.kuro.notiflow.presentation.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.models.settings.ColorType
import com.kuro.notiflow.domain.models.settings.LanguageType
import com.kuro.notiflow.domain.models.settings.SettingsModel
import com.kuro.notiflow.domain.models.settings.ThemeType
import com.kuro.notiflow.presentation.R
import com.kuro.notiflow.navigation.utils.AppNavigator
import com.kuro.notiflow.presentation.ui.settings.SettingsViewState

@Composable
internal fun SettingsContent(
    state: SettingsViewState,
    modifier: Modifier = Modifier,
    onUpdateSettings: (SettingsModel) -> Unit
) {
    if (state.settingsModel != null) {
        val uriHandler = LocalUriHandler.current
        val scrollState = rememberLazyListState()
        LazyColumn(
            state = scrollState,
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    MainSettingsSection(
                        languageType = state.settingsModel.language,
                        themeColors = state.settingsModel.themeType,
                        colorsType = state.settingsModel.colorsType,
                        dynamicColor = state.settingsModel.isDynamicColorEnabled,
                        onThemeColorUpdate = { colorsType ->
                            onUpdateSettings(state.settingsModel.copy(themeType = colorsType))
                        },
                        onLanguageChange = { language ->
                            onUpdateSettings(state.settingsModel.copy(language = language))
                        },
                        onColorsTypeUpdate = { colorsType ->
                            onUpdateSettings(state.settingsModel.copy(colorsType = colorsType))
                        },
                        onDynamicColorsChange = {
                            onUpdateSettings(state.settingsModel.copy(isDynamicColorEnabled = it))
                        },
                        onDataManagementClick = {
                            AppNavigator
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
                }
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    SecureSettingsSection(
                        secureMode = state.settingsModel.secureMode,
                        onUpdateSecureMode = {
                            onUpdateSettings(state.settingsModel.copy(secureMode = it))
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
    onDataManagementClick: () -> Unit
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
        DataManagement(
            onDataManagementClick = onDataManagementClick
        )
    }
}

@Composable
internal fun SecureSettingsSection(
    modifier: Modifier = Modifier,
    secureMode: Boolean,
    onUpdateSecureMode: (Boolean) -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = stringResource(R.string.secureSectionHeader),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
        )
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Row(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.secureModeTitle),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                )
                Switch(checked = secureMode, onCheckedChange = onUpdateSecureMode)
            }
        }
    }
}