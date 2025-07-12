package com.kuro.notiflow.presentation.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.kuro.notiflow.presentation.ui.home.components.OverviewSection

@Composable
fun HomeScreen() {
    val snackBarState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = paddingValues
            ) {
                item {
                    OverviewSection(Modifier)
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarState)
        }
    )
}
