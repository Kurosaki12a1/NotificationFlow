package com.kuro.notiflow.presentation.common

import android.content.res.Resources
import androidx.annotation.StringRes
import com.kuro.notiflow.navigation.model.Graph
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.presentation.common.ui.dialog.AppDialogSpec
import com.kuro.notiflow.presentation.common.ui.dialog.DialogController
import com.kuro.notiflow.navigation.utils.AppNavigator
import com.kuro.notiflow.presentation.common.ui.snackbar.SnackBarController
import com.kuro.notiflow.presentation.common.utils.SnackBarType

interface AppScope {
    /** Returns a localized string using the app-level [Resources] instance. */
    fun string(@StringRes id: Int, vararg formatArgs: Any): String

    /** Navigates to a top-level graph destination. */
    fun navigateGraph(graph: Graph)

    /** Navigates to a screen destination with optional back stack control. */
    fun navigateTo(
        route: Screen,
        popUpTo: Screen? = null,
        isInclusive: Boolean = false,
        isSingleTop: Boolean = true
    )

    /** Pops back to the specified route. */
    fun navigateBack(route: Screen, inclusive: Boolean = false)

    /** Pops the current destination from the back stack. */
    fun popBackStack()

    /** Shows a dialog through the shared dialog controller. */
    fun showDialog(dialog: AppDialogSpec)

    /** Hides the currently visible dialog, if any. */
    fun hideDialog()

    /** Shows a snackbar through the shared snackbar controller. */
    suspend fun showSnackBar(
        message: String,
        type: SnackBarType = SnackBarType.SUCCESS
    )
}

/**
 * Default [AppScope] implementation backed by app-level UI services.
 *
 * The dependencies are resolved once in [App] and then forwarded through regular
 * method calls. This keeps the scope API non-composable while still using Compose
 * to construct it at the root.
 */
internal class AppScopeInstance(
    private val resources: Resources,
    private val navigator: AppNavigator,
    private val dialogController: DialogController,
    private val snackBarController: SnackBarController
) : AppScope {
    override fun string(@StringRes id: Int, vararg formatArgs: Any): String {
        return resources.getString(id, *formatArgs)
    }

    override fun navigateGraph(graph: Graph) {
        navigator.navigateGraph(graph)
    }

    override fun navigateTo(
        route: Screen,
        popUpTo: Screen?,
        isInclusive: Boolean,
        isSingleTop: Boolean
    ) {
        navigator.navigateTo(route, popUpTo, isInclusive, isSingleTop)
    }

    override fun navigateBack(route: Screen, inclusive: Boolean) {
        navigator.navigateBack(route, inclusive)
    }

    override fun popBackStack() {
        navigator.popBackStack()
    }

    override fun showDialog(dialog: AppDialogSpec) {
        dialogController.show(dialog)
    }

    override fun hideDialog() {
        dialogController.hide()
    }

    override suspend fun showSnackBar(message: String, type: SnackBarType) {
        snackBarController.show(message, type)
    }
}
