package com.irpn.home.repository

import com.irpn.base.model.ServerResponseUsers
import com.irpn.home.api.HomeApi
import com.irpn.home.model.GithubUserResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response

class HomeRepositoryImpl(private val homeApi: HomeApi): HomeRepository {
    override fun getGithubUsersAsync(
        query: String,
        page: String,
        per_page: String
    ): Deferred<Response<ServerResponseUsers<GithubUserResponse>>> = homeApi.getUsersAsync(query, page, per_page)
}