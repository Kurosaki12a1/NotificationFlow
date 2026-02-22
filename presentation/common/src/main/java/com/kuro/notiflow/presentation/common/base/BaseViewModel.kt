package com.kuro.notiflow.presentation.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CancellationException

open class BaseViewModel : ViewModel() {
    protected val TAG: String
        get() = this::class.java.simpleName

    protected fun Exception.throwIfCancellation() {
        if (this is CancellationException) throw this
    }
}
