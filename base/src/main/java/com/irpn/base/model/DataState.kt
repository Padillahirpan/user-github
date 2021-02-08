package com.irpn.base.model

sealed class DataState<T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Failure<T>(val errorCode: Int?, val errorMessage: String, val detailedError: Map<String, String>, val errorTitle: String?) :
        DataState<T>()
    //data class SuccessList<T>(val data: List<T>, val meta: PaginationMeta<T>?): DataState<T>()
    data class SuccessList<T>(val data: List<T>): DataState<T>()
}