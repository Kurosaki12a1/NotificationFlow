package com.kuro.notiflow.presentation.common.ui.local

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

class TopBarScrollBehaviorHolder {
    @OptIn(ExperimentalMaterial3Api::class)
    var behavior: TopAppBarScrollBehavior? by mutableStateOf(null)
}

val LocalTopBarScrollBehavior = staticCompositionLocalOf<TopBarScrollBehaviorHolder> {
    error("No TopBarScrollBehaviorHolder provided")
}
