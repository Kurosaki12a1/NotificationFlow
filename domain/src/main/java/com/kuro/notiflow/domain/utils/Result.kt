package com.kuro.notiflow.domain.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

suspend fun <T> wrap(block: suspend () -> T): Result<T> {
    return try {
        Result.success(block.invoke())
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}

fun <T> wrapFlow(block: suspend () -> Flow<T>): Flow<Result<T>> = flow {
    block.invoke().catch { error -> emit(Result.failure(error)) }
        .collect { data -> emit(Result.success(data)) }
}

suspend fun <T> Result<T>.handle(
    onFailure: suspend (Throwable?) -> Unit = {},
    onSuccess: suspend (T?) -> Unit = {}
) {
    when {
        isSuccess -> onSuccess(getOrNull())
        else -> onFailure(exceptionOrNull())
    }
}

suspend fun <T> Flow<Result<T>>.collectAndHandle(
    onFailure: suspend (Throwable?) -> Unit = {},
    onSuccess: suspend (T?) -> Unit = {}
) = collect { data ->
    when {
        data.isSuccess -> onSuccess(data.getOrNull())
        else -> onFailure(data.exceptionOrNull())
    }
}