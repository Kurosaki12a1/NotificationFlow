package com.kuro.notiflow.presentation.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.presentation.R

@Composable
fun OverviewSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OverviewItem(
                title = stringResource(R.string.total_notifications),
                subTitle = "127",
                background = Color.Blue,
                icon = Icons.Default.Notifications
            )
            OverviewItem(
                title = stringResource(R.string.unread),
                subTitle = "23",
                background = Color.Green,
                icon = Icons.Default.Create
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OverviewItem(
                title = stringResource(R.string.today),
                subTitle = "15",
                background = Color.Red,
                icon = Icons.Default.FavoriteBorder
            )
            OverviewItem(
                title = stringResource(R.string.growth),
                subTitle = "12%",
                background = Color.DarkGray,
                icon = Icons.Default.Star
            )
        }
    }
}

@Composable
private fun RowScope.OverviewItem(
    title: String,
    subTitle: String,
    background: Color,
    icon: ImageVector
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .background(background, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = subTitle,
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(10.dp),
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.background
        )
    }

}