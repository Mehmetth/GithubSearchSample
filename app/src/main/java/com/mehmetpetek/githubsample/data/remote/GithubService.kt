package com.mehmetpetek.githubsample.data.remote

import com.mehmetpetek.githubsample.data.remote.model.SearchResponse
import com.mehmetpetek.githubsample.data.remote.model.UserDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("search/users")
    suspend fun search(
        @Query("q") query: String,
        @Query("page") page: String
    ): Response<SearchResponse>

    @GET("users/{login}")
    suspend fun userDetail(@Path("login") query: String): Response<UserDetailResponse>
}
