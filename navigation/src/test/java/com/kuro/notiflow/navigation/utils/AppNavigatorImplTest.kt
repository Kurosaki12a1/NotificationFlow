package com.kuro.notiflow.navigation.utils

import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.kuro.notiflow.navigation.NavigationConstants
import com.kuro.notiflow.navigation.model.Screen
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class AppNavigatorImplTest {

    private lateinit var navController: NavHostController
    private lateinit var navigator: AppNavigatorImpl

    @Before
    fun setUp() {
        mockkStatic("androidx.navigation.NavControllerKt")
        navController = mockk(relaxed = true)
        navigator = AppNavigatorImpl(navController)

        justRun { navController.navigate(any<Screen>(), any<NavOptionsBuilder.() -> Unit>()) }
        every { navController.popBackStack() } returns true
        every { navController.popBackStack(any<Screen>(), any<Boolean>(), any<Boolean>()) } returns true
    }

    @Test
    fun navigateTo_skips_whenSameRoute() {
        val destination = mockk<NavDestination>()
        every { destination.route } returns Screen.Home.toString()
        every { navController.currentDestination } returns destination

        navigator.navigateTo(Screen.Home)

        verify(exactly = 0) { navController.navigate(any<Screen>(), any<NavOptionsBuilder.() -> Unit>()) }
    }

    @Test
    fun navigateTo_navigates_whenDifferentRoute() {
        val destination = mockk<NavDestination>()
        every { destination.route } returns Screen.Notifications.toString()
        every { navController.currentDestination } returns destination

        navigator.navigateTo(Screen.Home)

        verify(exactly = 1) { navController.navigate(any<Screen>(), any<NavOptionsBuilder.() -> Unit>()) }
    }

    @Test
    fun navigateTo_throttles_rapid_calls() {
        every { navController.currentDestination } returns null

        navigator.navigateTo(Screen.Home)
        navigator.navigateTo(Screen.Notifications)

        verify(exactly = 1) { navController.navigate(any<Screen>(), any<NavOptionsBuilder.() -> Unit>()) }
    }

    @Test
    fun navigateTo_throttles_many_rapid_calls() {
        every { navController.currentDestination } returns null

        repeat(10) { navigator.navigateTo(Screen.Home) }

        verify(exactly = 1) { navController.navigate(any<Screen>(), any<NavOptionsBuilder.() -> Unit>()) }
    }

    @Test
    fun navigateTo_allows_after_delay() {
        every { navController.currentDestination } returns null

        navigator.navigateTo(Screen.Home)
        Thread.sleep(NavigationConstants.Delay.NAVIGATE + 20)
        navigator.navigateTo(Screen.Notifications)

        verify(exactly = 2) { navController.navigate(any<Screen>(), any<NavOptionsBuilder.() -> Unit>()) }
    }

    @Test
    fun navigateBack_calls_popBackStack() {
        every { navController.currentDestination } returns null

        navigator.navigateBack(Screen.Home, inclusive = true)

        verify(exactly = 1) { navController.popBackStack(any<Screen>(), true, any<Boolean>()) }
    }

    @Test
    fun popBackStack_throttles_rapid_calls() {
        navigator.popBackStack()
        navigator.popBackStack()

        verify(exactly = 1) { navController.popBackStack() }
    }

    @Test
    fun popBackStack_allows_after_delay() {
        navigator.popBackStack()
        Thread.sleep(NavigationConstants.Delay.NAVIGATE + 20)
        navigator.popBackStack()

        verify(exactly = 2) { navController.popBackStack() }
    }
}
