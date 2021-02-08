package com.irpn.base.extension

import com.irpn.base.exception.RequestException
import com.irpn.base.model.DataState
import com.irpn.base.model.Result
import com.irpn.base.model.errorresponse.DefaultErrorResponse
import com.irpn.base.model.errorresponse.DynamicErrorResponse
import com.irpn.base.model.errorresponse.JsonApiErrorResponse
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Deferred
import retrofit2.Response
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

suspend inline fun <T> Deferred<Response<T>>.awaitAndGet(): Result<T?> {
    return try {
        val response = await()
        if (response.isSuccessful) Result.Success(response.body(), response.code())
        else {
            val errorBody = response.errorBody()?.source()?.readUtf8()
            Timber.e(errorBody)
            val moshi = Moshi.Builder().build()

            val jsonAdapter = moshi.adapter(DefaultErrorResponse::class.java)
            val errorResponse = try {
                jsonAdapter.fromJson(errorBody)?.message
            } catch (e: Exception) {
                null
            }

            val jsonDynamicAdapter = moshi.adapter(DynamicErrorResponse::class.java)
            val jsonDynamicErrorResponse = try {
                jsonDynamicAdapter.fromJson(errorBody).errors.values.flatten().joinToString(", ")
            } catch (e: Exception) {
                null
            }

            val jsonApiAdapter = moshi.adapter(JsonApiErrorResponse::class.java)
            /*val jsonApiErrorResponse = try { jsonApiAdapter.fromJson(errorBody)?.let { it.meta ?: it.errorList[0].detail }
            } catch (e: Exception) { null }*/
            val jsonApiErrorResponse: JsonApiErrorResponse? = try {
                jsonApiAdapter.fromJson(errorBody)
            } catch (e: Exception) {
                null
            }

            if (jsonApiErrorResponse != null) {
                val titleBuilder = StringBuilder()
                jsonApiErrorResponse.errorList.forEach {
                    if (!it.title.isNullOrEmpty())
                        titleBuilder.append(it.title).append(" ")
                }
                Result.Failure(
                    RequestException(
                        response.code(),
                        jsonApiErrorResponse.errorList.associate {
                            it.source?.parameter.orEmpty() to it.detail.capitalize()
                        }, titleBuilder.toString()
                    )
                )
            } else {
                val responseMessage = errorResponse ?: jsonDynamicErrorResponse

                Result.Failure(
                    RequestException(
                        response.code(), mapOf("" to responseMessage.orEmpty()), null
                    )
                )
            }

        }
    } catch (e: Exception) {
        when (e) {
            is UnknownHostException,
            is SocketException,
            is SocketTimeoutException,
            is TimeoutException,
            is ConnectException -> {
            }
            else -> Timber.tag("Network Request").e(e)
        }
        Result.Failure(e)
    }
}


//suspend inline fun <T, K> Deferred<Response<T>>.awaitAndGet2(): Event<DataState<K>>? {
//    return when (val result = awaitAndGet()) {
//        is Result.Success -> {
//            result.body?.let {
//                if (it is ServerResponse<*>) {
//                    return Event(DataState.Success((it as ServerResponse<K>).data))
//                } else if (it is PaginationServerResponse<*>) {
//                    (it as? PaginationServerResponse<K>)?.run {
//                        return Event(DataState.SuccessList(data, meta))
//                    }
//                }
//            }
//            return null
//        }
//        is Result.Failure -> {
//            Event(toFailureDataState(result.exception))
//        }
//    }
//}

fun <T> toFailureDataState(exception: Throwable) = when (exception) {
    is UnknownHostException,
    is SocketException,
    is SocketTimeoutException,
    is TimeoutException,
    is ConnectException -> DataState.Failure(
        null,
        "Internet connection not available",
        emptyMap(),
        null
    )
    is RequestException -> DataState.Failure(
        exception.errorCode,
        if (exception.localizedMessage.isEmpty()) "We are experiencing problem to connect to our server, please try again later&#8230;" else exception.localizedMessage,
        exception.errorMap
        , exception.getLocalizedTitle()
    )
    else -> DataState.Failure<T>(
        exception.errorCode,
        "We are experiencing problem to connect to our server, please try again later&#8230;",
        emptyMap(),
        null
    )
}