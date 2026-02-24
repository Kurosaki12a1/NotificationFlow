package com.kuro.notiflow.presentation.onboarding.ui

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.core.graphics.drawable.toBitmap
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.navigation.model.Graph
import com.kuro.notiflow.navigation.utils.AppNavigator
import com.kuro.notiflow.presentation.common.extensions.scrollText
import com.kuro.notiflow.presentation.common.ui.dialog.ConfirmDialogSpec
import com.kuro.notiflow.presentation.common.ui.dialog.DialogController
import com.kuro.notiflow.presentation.common.ui.local.LocalDialogController
import com.kuro.notiflow.presentation.common.ui.local.LocalNavigator
import com.kuro.notiflow.presentation.common.utils.PermissionUtils
import com.kuro.notiflow.presentation.onboarding.R

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val dialogController = LocalDialogController.current
    val navigator = LocalNavigator.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasListener by remember {
        mutableStateOf(
            PermissionUtils.isNotificationListenerEnabled(
                context
            )
        )
    }
    var hasPostPermission by remember {
        mutableStateOf(
            PermissionUtils.hasPostNotificationsPermission(
                context
            )
        )
    }

    val postPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPostPermission = granted
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasListener = PermissionUtils.isNotificationListenerEnabled(context)
                hasPostPermission = PermissionUtils.hasPostNotificationsPermission(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val appIconBitmap = remember(context) {
            context.applicationInfo.loadIcon(context.packageManager).toBitmap()
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                modifier = Modifier.size(40.dp),
                bitmap = appIconBitmap.asImageBitmap(),
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .scrollText(),
                text = stringResource(R.string.onboarding_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
        Text(
            text = stringResource(R.string.onboarding_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        PermissionCard(
            title = stringResource(R.string.onboarding_listener_title),
            description = stringResource(R.string.onboarding_listener_desc),
            isGranted = hasListener,
            onAction = {
                AppLog.i(TAG, "openNotificationListenerSettings")
                openNotificationListenerSettings(context)
            }
        )

        if (PermissionUtils.needsPostNotificationsPermission()) {
            PermissionCard(
                title = stringResource(R.string.onboarding_post_title),
                description = stringResource(R.string.onboarding_post_desc),
                isGranted = hasPostPermission,
                onAction = {
                    AppLog.i(TAG, "requestPostNotificationsPermission")
                    postPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier.widthIn(min = 180.dp, max = 240.dp),
                onClick = {
                    AppLog.i(TAG, "continue")
                    handleContinue(
                        context = context,
                        hasListener = hasListener,
                        dialogController = dialogController,
                        onComplete = { completeOnboarding(viewModel, navigator) }
                    )
                }
            ) {
                Text(text = stringResource(R.string.onboarding_continue))
            }
        }
    }
}

private const val TAG = "OnboardingScreen"

@Composable
private fun PermissionCard(
    title: String,
    description: String,
    isGranted: Boolean,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val statusText = if (isGranted) {
                    stringResource(R.string.onboarding_granted)
                } else {
                    stringResource(R.string.onboarding_not_granted)
                }
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isGranted) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
                Button(
                    onClick = onAction,
                    enabled = !isGranted
                ) {
                    Text(text = stringResource(R.string.onboarding_action))
                }
            }
        }
    }
}

private fun openNotificationListenerSettings(context: Context) {
    context.startActivity(PermissionUtils.notificationListenerSettingsIntent())
}

private fun completeOnboarding(
    viewModel: OnboardingViewModel,
    navigator: AppNavigator
) {
    viewModel.completeOnboarding()
    navigator.navigateGraph(Graph.HomeGraph)
}

private fun handleContinue(
    context: Context,
    hasListener: Boolean,
    dialogController: DialogController,
    onComplete: () -> Unit
) {
    if (!hasListener) {
        dialogController.show(
            ConfirmDialogSpec(
                title = context.getString(R.string.onboarding_listener_missing_title),
                message = context.getString(R.string.onboarding_listener_missing_message),
                confirmText = context.getString(R.string.onboarding_open_settings),
                cancelText = context.getString(R.string.onboarding_continue_anyway),
                onConfirm = { openNotificationListenerSettings(context) },
                onDismiss = { onComplete() },
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )
        )
    } else {
        onComplete()
    }
}
