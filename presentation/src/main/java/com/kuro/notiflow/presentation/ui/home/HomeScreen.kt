package com.kuro.notiflow.presentation.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuro.notiflow.domain.Constants.Home.OVERVIEW_KEY
import com.kuro.notiflow.domain.Constants.Home.RECENT_NOTIFICATION
import com.kuro.notiflow.domain.Constants.Home.STATISTIC_KEY
import com.kuro.notiflow.presentation.common.utils.Utils
import com.kuro.notiflow.presentation.ui.home.components.OverviewSection
import com.kuro.notiflow.presentation.ui.home.components.RecentNotificationsSection
import com.kuro.notiflow.presentation.ui.home.components.StatisticSection

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = OVERVIEW_KEY) {
            OverviewSection(state)
        }
        item(key = STATISTIC_KEY) {
            StatisticSection(packageStats = Utils.getTopRecentNotifications(state.listNotifications))
        }
        item(key = RECENT_NOTIFICATION) {
            RecentNotificationsSection(
                listNotifications = state.listNotifications.sortedByDescending { it.postTime }.take(5)
            )
        }
    }
}
