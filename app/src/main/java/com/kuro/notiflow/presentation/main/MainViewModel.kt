package com.kuro.notiflow.presentation.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.kuro.notiflow.domain.Constants.Delay.NAVIGATION_FLOW
import com.kuro.notiflow.presentation.common.navigation.Screen

class MainViewModel : ViewModel() {
    private val _backStack = mutableStateListOf<Screen>(Screen.Home)
    val backStack: List<Screen> get() = _backStack

    private var lastClickTime = 0L

    fun navigateTo(screen: Screen) {
        val now = System.currentTimeMillis()
        if (now - lastClickTime < NAVIGATION_FLOW) return
        lastClickTime = now
        if (screen !is Screen.Home) {
            _backStack.removeAll { it != Screen.Home }
            _backStack.add(screen)
        } else {
            _backStack.clear()
            _backStack.add(Screen.Home)
        }
    }

    fun popBackStack() {
        _backStack.removeLastOrNull()
    }
}