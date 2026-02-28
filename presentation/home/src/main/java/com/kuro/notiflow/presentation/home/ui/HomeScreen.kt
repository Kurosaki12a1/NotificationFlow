package com.kuro.notiflow.presentation.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.domain.Constants.Home.OVERVIEW_KEY
import com.kuro.notiflow.domain.Constants.Home.RECENT_NOTIFICATION
import com.kuro.notiflow.domain.Constants.Home.STATISTIC_KEY
import com.kuro.notiflow.presentation.common.extensions.takeFirst
import com.kuro.notiflow.presentation.common.ui.local.LocalNavigator
import com.kuro.notiflow.presentation.home.ui.components.OverviewSection
import com.kuro.notiflow.presentation.home.ui.components.RecentNotificationsSection
import com.kuro.notiflow.presentation.home.ui.components.StatisticSection

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val data = viewModel.listNotifications.collectAsLazyPagingItems()
    val navigator = LocalNavigator.current
    val topPackages by viewModel.topNotifications.collectAsStateWithLifecycle()
    val overViewStats by viewModel.overviewNotificationStats.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = OVERVIEW_KEY) {
            OverviewSection(overViewStats)
        }
        item(key = STATISTIC_KEY) {
            StatisticSection(packageStats = topPackages)
        }
        item(key = RECENT_NOTIFICATION) {
            RecentNotificationsSection(
                listNotifications = data.takeFirst(5),
                onViewAllClick = { navigator.navigateTo(Screen.Notifications) }
            )
        }
    }
}
