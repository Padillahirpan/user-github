package com.irpn.base.extension

import com.irpn.base.exception.RequestException

fun Throwable.throwException(): Nothing = throw this

val Throwable.errorCode
    get() = when (this) {
        is RequestException -> statusCode
        else -> null
    }