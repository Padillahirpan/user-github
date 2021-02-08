package com.irpn.base.model

sealed class Result<T> {

    data class Success<T>(val body: T, val code: Int) : Result<T>()
    data class Failure<T>(val exception: Throwable) : Result<T>()
}