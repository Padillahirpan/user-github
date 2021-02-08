package com.irpn.home.api

import com.irpn.base.model.ServerResponseUsers
import com.irpn.home.model.GithubUserResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {

    @GET("search/users")
    fun getUsersAsync(
        @Query("q") q: String,
        @Query("page") page: String,
        @Query("per_page") per_page: String
    ): Deferred<Response<ServerResponseUsers<GithubUserResponse>>>
}