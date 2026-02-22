package com.kuro.notiflow.presentation.common.ui.main

import android.content.Context
import android.view.WindowManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.common.MainActivity
import com.kuro.notiflow.presentation.common.navigation.MainNavGraph
import com.kuro.notiflow.presentation.common.theme.NotificationFlowTheme
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.common.ui.dialog.AppDialogHost
import com.kuro.notiflow.presentation.common.ui.local.LocalNavigator
import com.kuro.notiflow.presentation.common.ui.local.LocalSnackBarController
import com.kuro.notiflow.presentation.common.ui.main.components.AppTopBar
import com.kuro.notiflow.presentation.common.ui.snackbar.DefaultSnackBar
import com.kuro.notiflow.presentation.common.ui.snackbar.ErrorSnackBar
import com.kuro.notiflow.presentation.common.utils.SnackBarType
import com.kuro.notiflow.presentation.common.view.BottomNavigationBar
import com.kuro.notiflow.presentation.common.view.BottomNavigationItem
import com.kuro.notiflow.presentation.common.R as CommonR

@Composable
fun MainScreen(
    navController: NavHostController,
    features: Set<FeatureNav>,
    topBarProviders: Set<TopBarProvider>,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current
    val context: Context = LocalContext.current
    val snackBarController = LocalSnackBarController.current
    val window = (context as? MainActivity)?.window
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(state.settingsModel?.secureMode) {
        val secureMode = state.settingsModel?.secureMode ?: return@LaunchedEffect
        if (secureMode) {
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        } else {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    val settings = state.settingsModel
    if (settings == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(CommonR.string.loading),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }

    NotificationFlowTheme(
        languageType = settings.language,
        themeType = settings.themeType,
        colorType = settings.colorsType,
        dynamicColor = settings.isDynamicColorEnabled,
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeContent,
            topBar = {
                AppTopBar(navController, topBarProviders)
            },
            bottomBar = {
                if (currentBackStackEntry?.destination?.parent?.route in BottomNavigationItem.entries.map { it.destination.toString() }) {
                    BottomNavigationBar(
                        modifier = Modifier,
                        selectedItem = currentBackStackEntry?.destination?.parent?.route,
                        items = BottomNavigationItem.entries.toTypedArray(),
                        showLabel = true,
                        onItemSelected = { navigator.navigateGraph(it.destination) }
                    )
                }
            },
            content = { paddingValues ->
                MainNavGraph(
                    navController = navController,
                    paddingValues = PaddingValues(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding(),
                        start = 16.dp + paddingValues.calculateLeftPadding(LayoutDirection.Ltr),
                        end = 16.dp + paddingValues.calculateRightPadding(LayoutDirection.Ltr)
                    ),
                    features = features
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarController.hostState,
                    snackbar = { data ->
                        when (snackBarController.type) {
                            SnackBarType.ERROR -> ErrorSnackBar(data)
                            SnackBarType.SUCCESS -> DefaultSnackBar(data)
                        }
                    }
                )
            }
        )
        AppDialogHost()
    }
}
