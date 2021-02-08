package com.irpn.home.repository

import com.irpn.base.model.ServerResponseUsers
import com.irpn.home.model.GithubUserResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface HomeRepository {
    fun getGithubUsersAsync(query: String, page: String, per_page: String): Deferred<Response<ServerResponseUsers<GithubUserResponse>>>
}