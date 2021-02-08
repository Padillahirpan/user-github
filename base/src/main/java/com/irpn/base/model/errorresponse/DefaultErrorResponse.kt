package com.irpn.base.model.errorresponse

import com.squareup.moshi.Json

data class
DefaultErrorResponse(
    @Json(name = "type") val type: String?,
    @Json(name = "meta") val message: String
)