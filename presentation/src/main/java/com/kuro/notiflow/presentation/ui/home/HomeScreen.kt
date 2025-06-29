package com.kuro.notiflow.presentation.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kuro.notiflow.presentation.ui.home.components.OverviewSection

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp)
    ) {
        OverviewSection()
    }
}
