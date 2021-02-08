package com.irpn.base.model.errorresponse

import com.squareup.moshi.Json

data class SingleErrorResponse(
    @Json(name = "error") val error: String
)