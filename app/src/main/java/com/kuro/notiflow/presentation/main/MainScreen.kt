package com.kuro.notiflow.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.kuro.notiflow.presentation.common.navigation.Screen
import com.kuro.notiflow.presentation.common.view.BottomNavigationBar
import com.kuro.notiflow.presentation.common.view.BottomNavigationItem

@Composable
fun MainScreen(
    backStack: List<Screen>,
    selectedItem: String,
    onPopBackStack: () -> Unit,
    onItemSelected: (Screen) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeContent,
        topBar = {

        },
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier,
                selectedItem = selectedItem,
                items = BottomNavigationItem.entries.toTypedArray(),
                showLabel = true,
                onItemSelected = { onItemSelected(it.destination) }
            )
        },
        content = { paddingValues ->
            NavDisplay(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                backStack = backStack,
                entryDecorators =
                    listOf(
                        rememberSceneSetupNavEntryDecorator(),
                        rememberSavedStateNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                onBack = { onPopBackStack() },
                entryProvider = entryProvider {
                    entry<Screen.Home> {
                        Box(modifier = Modifier.background(Color.Red)) {
                            Text("This is home screen")
                        }
                    }
                    entry<Screen.Settings> {
                        Box(modifier = Modifier.background(Color.Blue)) {
                            Text("This is settings screen")
                        }
                    }
                    entry<Screen.Notifications> {
                        Box(modifier = Modifier.background(Color.Green)) {
                            Text("This is notifications screen")
                        }
                    }
                }
            )
        }
    )
}