package com.kuro.notiflow.presentation.common.ui.local

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * A state holder for [TopAppBarScrollBehavior] to be used within a [CompositionLocal].
 *
 * This wrapper allows different screens or components to provide and update the
 * scroll behavior of a top-level Scaffold's TopBar dynamically. Using [mutableStateOf]
 * ensures that any changes to the behavior property trigger recomposition in
 * components observing this holder.
 */
class TopBarScrollBehaviorHolder {
    /**
     * The current scroll behavior for the TopBar.
     *
     * Initialized as null and can be set once the Scaffold or TopBar is initialized
     * within the composition.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    var behavior: TopAppBarScrollBehavior? by mutableStateOf(null)
}

val LocalTopBarScrollBehavior = staticCompositionLocalOf<TopBarScrollBehaviorHolder> {
    error("No TopBarScrollBehaviorHolder provided")
}
