package com.kuro.notiflow.presentation.ui.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.R
import com.kuro.notiflow.presentation.common.theme.LanguageType
import com.kuro.notiflow.presentation.common.view.DialogButtons

@Composable
internal fun LanguageChooser(
    modifier: Modifier = Modifier,
    language: LanguageType,
    onLanguageChanged: (LanguageType) -> Unit,
) {
    var isOpenDialog by rememberSaveable { mutableStateOf(false) }
    Surface(
        onClick = { isOpenDialog = true },
        modifier = modifier.padding(vertical = 10.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.mainSettingsLanguageTitle),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                modifier = Modifier.weight(1f),
                text = language.toLanguageName(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
    LanguageDialogChooser(
        openDialog = isOpenDialog,
        initialLanguage = language,
        onCloseDialog = { isOpenDialog = false },
        onLanguageChoose = { languageType ->
            isOpenDialog = false
            onLanguageChanged(languageType)
        },
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LanguageDialogChooser(
    modifier: Modifier = Modifier,
    openDialog: Boolean,
    initialLanguage: LanguageType,
    onCloseDialog: () -> Unit,
    onLanguageChoose: (LanguageType) -> Unit,
) {
    if (openDialog) {
        val initPosition = LanguageType.entries.indexOf(initialLanguage)
        val listState = rememberLazyListState(initPosition)
        var selectedLanguage by rememberSaveable { mutableStateOf(initialLanguage) }

        BasicAlertDialog(onDismissRequest = onCloseDialog) {
            Surface(
                modifier = modifier
                    .width(280.dp)
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surfaceContainer,
            ) {
                Column {
                    Box(
                        modifier = Modifier.padding(
                            start = 24.dp,
                            end = 24.dp,
                            top = 24.dp,
                            bottom = 12.dp
                        ),
                    ) {
                        Text(
                            text = stringResource(R.string.mainSettingsLanguageTitle),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                    LazyColumn(modifier = Modifier.height(300.dp), state = listState) {
                        items(LanguageType.entries.toTypedArray()) { language ->
                            LanguageDialogItem(
                                modifier = Modifier.fillMaxWidth(),
                                title = language.toLanguageName(),
                                selected = selectedLanguage == language,
                                onSelectChange = { selectedLanguage = language },
                            )
                        }
                    }
                    DialogButtons(
                        onCancelClick = onCloseDialog,
                        onConfirmClick = { onLanguageChoose.invoke(selectedLanguage) },
                    )
                }
            }
        }
    }
}

@Composable
internal fun LanguageDialogItem(
    modifier: Modifier = Modifier,
    title: String,
    selected: Boolean,
    onSelectChange: () -> Unit,
) {
    Column {
        Row(
            modifier = modifier
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .clip(MaterialTheme.shapes.medium)
                .clickable(onClick = onSelectChange)
                .padding(start = 8.dp, end = 16.dp)
                .requiredHeight(56.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
            )
            RadioButton(selected = selected, onClick = null)
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}
