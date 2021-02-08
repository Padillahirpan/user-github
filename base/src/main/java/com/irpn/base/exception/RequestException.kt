package com.irpn.base.exception

typealias KeyedErrorMap = Map<String, String>

open class RequestException(val statusCode: Int, val errorMap: KeyedErrorMap,val title: String?) : Exception() {

    private val fullMessage = errorMap.values.joinToString()

    companion object {
        private const val NO_NETWORK_CODE = -1
    }

    override fun getLocalizedMessage(): String {
        return fullMessage
    }

    fun getLocalizedTitle():String {
        return title.orEmpty()
    }
}