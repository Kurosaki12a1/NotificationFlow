package com.kuro.notiflow.domain.logger


object AppLog {
    @Volatile
    private var logger: Logger? = null

    fun setLogger(newLogger: Logger?) {
        logger = newLogger
    }

    fun d(tag: String, message: String) = logger?.d(tag, message)
    fun i(tag: String, message: String) = logger?.i(tag, message)
    fun w(tag: String, message: String, throwable: Throwable? = null) =
        logger?.w(tag, message, throwable)

    fun e(tag: String, message: String, throwable: Throwable? = null) =
        logger?.e(tag, message, throwable)
}
