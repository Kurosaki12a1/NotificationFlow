package com.kuro.notiflow.presentation.settings.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.domain.models.settings.ColorType
import com.kuro.notiflow.presentation.common.R
import com.kuro.notiflow.presentation.common.extensions.onSeed
import com.kuro.notiflow.presentation.common.extensions.seed

@Composable
internal fun ColorsTypeChooser(
    modifier: Modifier = Modifier,
    colorsType: ColorType,
    onChoose: (ColorType) -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.mainSettingsColorsTitle),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                for (color in ColorType.entries) {
                    ColorTypeItem(
                        modifier = Modifier.weight(1f),
                        model = color,
                        selected = colorsType == color,
                        onClick = { if (colorsType != color) onChoose(color) },
                    )
                }
            }
        }
    }
}

@Composable
internal fun ColorTypeItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    model: ColorType,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .widthIn(max = 80.dp),
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        color = model.seed(),
    ) {
        if (selected) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
                color = model.onSeed(),
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
            }
        }
    }
}
