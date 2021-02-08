package com.irpn.base.model

import com.squareup.moshi.Json

data class ServerResponse<T>(
    @Json(name = "code") val code: Int,
    @Json(name = "message") val message: String,
    @Json(name = "result") val meta: DataResponse<T>
)

data class ServerResponseUsers<T>(
    @Json(name = "total_count") val total_count: Int,
    @Json(name = "incomplete_results") val incomplete_results: Boolean,
    @Json(name = "items") val result: List<T>
)

data class ServerError(
    @Json(name = "status") val status: Int,
    @Json(name = "code") val code: Int,
    @Json(name = "source") val source: Source,
    @Json(name = "title") val title: String = "title",
    @Json(name = "detail") val detail: String
)

data class DataResponse<T>(
    @Json(name = "current_page") val currentPage: String,
    @Json(name = "data") val data: List<T>,
    @Json(name = "total") val total: Int
)
data class Source(@Json(name = "meta") val message: String, val line: Int)
