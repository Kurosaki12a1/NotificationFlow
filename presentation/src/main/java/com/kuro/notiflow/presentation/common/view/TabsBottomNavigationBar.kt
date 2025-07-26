package com.kuro.notiflow.presentation.common.view

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.kuro.notiflow.presentation.R
import com.kuro.notiflow.presentation.common.extensions.scrollText
import com.kuro.notiflow.presentation.common.navigation.Screen


@Composable
fun <Item : BottomBarItem> BottomNavigationBar(
    modifier: Modifier,
    selectedItem: String?,
    items: Array<Item>,
    showLabel: Boolean,
    onItemSelected: (Item) -> Unit
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        items.forEach { item ->
            val isSelected = selectedItem == item.destination.toString()
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected.invoke(item) },
                icon = {
                    BottomBarIcon(
                        selected = isSelected,
                        enabledIcon = item.enabledIcon,
                        disabledIcon = item.disabledIcon,
                        description = item.label
                    )
                },
                label = {
                    if (showLabel) {
                        BottomBarLabel(
                            selected = selectedItem == item.destination.toString(),
                            title = item.label
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                )
            )
        }
    }
}

@Composable
private fun BottomBarIcon(
    selected: Boolean,
    enabledIcon: Painter,
    disabledIcon: Painter,
    description: String
) {
    Icon(
        painter = if (selected) enabledIcon else disabledIcon,
        contentDescription = description,
        tint = when (selected) {
            true -> MaterialTheme.colorScheme.onSecondaryContainer
            false -> MaterialTheme.colorScheme.onSurfaceVariant
        }
    )
}

@Composable
private fun BottomBarLabel(
    selected: Boolean,
    title: String
) {
    Text(
        modifier = Modifier.scrollText(),
        text = title,
        color = when (selected) {
            true -> MaterialTheme.colorScheme.onSurface
            false -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
    )
}

enum class BottomNavigationItem : BottomBarItem {
    HOME {
        override val destination: Screen
            get() = Screen.Home
        override val enabledIcon: Painter
            @Composable get() = painterResource(R.drawable.ic_home)
        override val disabledIcon: Painter
            @Composable get() = painterResource(R.drawable.ic_home_outlined)
        override val label: String
            @Composable get() = stringResource(R.string.homeTabTitle)
    },
    NOTIFICATIONS {
        override val destination: Screen
            get() = Screen.Notifications
        override val enabledIcon: Painter
            @Composable get() = painterResource(R.drawable.ic_notifications)
        override val disabledIcon: Painter
            @Composable get() = painterResource(R.drawable.ic_notifications_outline)
        override val label: String
            @Composable get() = stringResource(R.string.notificationsTabTitle)
    },
    SETTINGS {
        override val destination: Screen
            get() = Screen.Settings
        override val enabledIcon: Painter
            @Composable get() = painterResource(R.drawable.ic_settings)
        override val disabledIcon: Painter
            @Composable get() = painterResource(R.drawable.ic_settings_outline)
        override val label: String
            @Composable get() = stringResource(R.string.settingsTabTitle)
    }
}

interface BottomBarItem {
    val destination: Screen
    val label: String @Composable get
    val enabledIcon: Painter @Composable get
    val disabledIcon: Painter @Composable get
}