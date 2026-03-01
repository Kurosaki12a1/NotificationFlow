package com.kuro.notiflow.presentation.settings.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.presentation.common.ui.local.LocalNavigator
import com.kuro.notiflow.presentation.common.utils.PermissionUtils
import com.kuro.notiflow.presentation.settings.ui.settings.components.SettingsContent

@Composable
internal fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val isNotificationListenerEnabled = remember {
        mutableStateOf(PermissionUtils.isNotificationListenerEnabled(context))
    }
    val navigator = LocalNavigator.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isNotificationListenerEnabled.value =
                    PermissionUtils.isNotificationListenerEnabled(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    SettingsContent(
        state = state,
        modifier = Modifier,
        onUpdateSettings = {
            viewModel.updateSettings(it)
        },
        onDataManagementClick = { navigator.navigateTo(Screen.DataManagement) },
        isNotificationListenerEnabled = isNotificationListenerEnabled.value,
        onNotificationListenerClick = {
            context.startActivity(PermissionUtils.notificationListenerSettingsIntent())
        },
        onNotificationFiltersClick = {
            // Keep the entry visible now so the settings structure can settle
            // before the dedicated filter screen is wired in.
            AppLog.d(TAG, "openNotificationFilters")
        }
    )
}

private const val TAG = "SettingsScreen"
