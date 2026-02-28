package com.kuro.notiflow.presentation.bookmark.ui.rules

import com.kuro.notiflow.presentation.common.utils.SnackBarType

sealed interface BookmarkRulesEvent {
    data class ShowSnackBar(
        val messageResId: Int,
        val type: SnackBarType = SnackBarType.SUCCESS
    ) : BookmarkRulesEvent
}
