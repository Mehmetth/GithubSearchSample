package com.mehmetpetek.githubsample.data.remote

import javax.inject.Inject

class RetrofitDataSource @Inject constructor(private val githubService: GithubService) {

    suspend fun search(query: String) = githubService.search(
        query
    )

    suspend fun userDetail(login: String) = githubService.userDetail(login)
}