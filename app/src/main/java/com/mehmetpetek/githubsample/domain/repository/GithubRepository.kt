package com.mehmetpetek.githubsample.domain.repository

import com.mehmetpetek.githubsample.data.remote.model.Resource
import com.mehmetpetek.githubsample.data.remote.model.SearchResponse
import com.mehmetpetek.githubsample.data.remote.model.UserDetailResponse
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    fun search(query: String): Flow<Resource<SearchResponse>>
    fun userDetail(login: String): Flow<Resource<UserDetailResponse>>
}