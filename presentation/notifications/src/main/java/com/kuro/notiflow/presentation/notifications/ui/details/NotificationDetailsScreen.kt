package com.kuro.notiflow.presentation.notifications.ui.details

import android.content.Context
import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuro.notiflow.domain.Constants.Details.ACTION_KEY
import com.kuro.notiflow.domain.Constants.Details.DETAIL_KEY
import com.kuro.notiflow.domain.Constants.Details.GENERAL_KEY
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.presentation.common.ui.dialog.DialogController
import com.kuro.notiflow.presentation.common.ui.dialog.ConfirmDialogSpec
import com.kuro.notiflow.presentation.common.ui.local.LocalDialogController
import com.kuro.notiflow.presentation.common.ui.local.LocalNavigator
import com.kuro.notiflow.presentation.common.ui.local.LocalSnackBarController
import com.kuro.notiflow.presentation.common.extensions.getAppName
import com.kuro.notiflow.presentation.common.utils.Utils.shareText
import com.kuro.notiflow.presentation.common.utils.Utils.convertMillisToTimeDetails
import com.kuro.notiflow.presentation.notifications.ui.details.components.ActionNotifications
import com.kuro.notiflow.presentation.notifications.ui.details.components.DetailsInformationNotifications
import com.kuro.notiflow.presentation.notifications.ui.details.components.GeneralNotifications
import kotlinx.coroutines.flow.collectLatest
import com.kuro.notiflow.presentation.notifications.R
import com.kuro.notiflow.presentation.common.R as CommonR

@Composable
internal fun NotificationDetailsScreen(
    notificationId: Long,
    viewModel: NotificationDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle(NotificationDetailsState())
    val context = LocalContext.current
    val snackBarController = LocalSnackBarController.current
    val resources = LocalResources.current
    val navigator = LocalNavigator.current
    val dialogController = LocalDialogController.current

    LaunchedEffect(Unit) {
        viewModel.getNotification(notificationId)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is NotificationDetailsEvent.ShowSnackBar -> {
                    snackBarController.show(
                        message = resources.getString(event.messageResId),
                        type = event.type
                    )
                }
                is NotificationDetailsEvent.ShareNotification -> {
                    shareText(
                        context = context,
                        text = buildShareText(
                            context = context,
                            resources = resources,
                            notification = event.notification
                        ),
                        chooserTitle = resources.getString(R.string.share)
                    )
                }
                NotificationDetailsEvent.NavigateBack -> {
                    navigator.popBackStack()
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = GENERAL_KEY) {
            GeneralNotifications(state.notification)
        }
        item(key = DETAIL_KEY) {
            DetailsInformationNotifications(state.notification)
        }
        item(key = ACTION_KEY) {
            ActionNotifications(
                notification = state.notification,
                onSeeMore = { packageName ->
                    AppLog.d(TAG, "seeMore pkg=$packageName")
                    viewModel.onSeeMoreClick(packageName)
                },
                onBlockFromApp = { packageName ->
                    state.notification?.let { notification ->
                        AppLog.i(
                            TAG,
                            "blockApp id=${notification.id} pkg=${notification.packageName}"
                        )
                    }
                    viewModel.onBlockFromAppClicked(packageName)
                },
                onBookmarkClicked = { shouldBookmark ->
                    state.notification?.let { notification ->
                        AppLog.i(
                            TAG,
                            "toggleBookmark id=${notification.id} pkg=${notification.packageName}"
                        )
                    }
                    viewModel.onBookmarkClicked(shouldBookmark)
                },
                onDelete = { id ->
                    state.notification?.let { notification ->
                        AppLog.i(
                            TAG,
                            "delete id=${notification.id} pkg=${notification.packageName}"
                        )
                    }
                    showDeleteDialog(
                        dialogController = dialogController,
                        resources = resources,
                        onConfirm = { viewModel.onDeleteClick(id) }
                    )
                }
            )
        }
    }
}

private fun showDeleteDialog(
    dialogController: DialogController,
    resources: Resources,
    onConfirm: () -> Unit
) {
    dialogController.show(
        ConfirmDialogSpec(
            title = resources.getString(R.string.delete_notification_title),
            message = resources.getString(R.string.delete_notification_message),
            confirmText = resources.getString(CommonR.string.okConfirmTitle),
            cancelText = resources.getString(CommonR.string.cancelTitle),
            onConfirm = onConfirm
        )
    )
}

private fun buildShareText(
    context: Context,
    resources: Resources,
    notification: NotificationModel
): String {
    return buildList {
        add(
            resources.getString(
                R.string.share_text_app,
                notification.packageName.getAppName(context)
            )
        )
        notification.title
            ?.takeIf { it.isNotBlank() }
            ?.let { add(resources.getString(R.string.share_text_title, it)) }
        notification.text
            ?.takeIf { it.isNotBlank() }
            ?.let { add(resources.getString(R.string.share_text_message, it)) }
        notification.subText
            ?.takeIf { it.isNotBlank() }
            ?.let { add(resources.getString(R.string.share_text_subtext, it)) }
        notification.bigText
            ?.takeIf { it.isNotBlank() }
            ?.let { add(resources.getString(R.string.share_text_expanded, it)) }
        notification.summaryText
            ?.takeIf { it.isNotBlank() }
            ?.let { add(resources.getString(R.string.share_text_summary, it)) }
        notification.infoText
            ?.takeIf { it.isNotBlank() }
            ?.let { add(resources.getString(R.string.share_text_info, it)) }
        notification.textLines
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.takeIf { it.isNotEmpty() }
            ?.let { lines ->
                add(
                    resources.getString(
                        R.string.share_text_lines,
                        lines.joinToString(separator = "\n")
                    )
                )
            }
        add(
            resources.getString(
                R.string.share_text_received,
                convertMillisToTimeDetails(notification.postTime)
            )
        )
    }.joinToString(separator = "\n")
}

private const val TAG = "NotificationDetailsScreen"
