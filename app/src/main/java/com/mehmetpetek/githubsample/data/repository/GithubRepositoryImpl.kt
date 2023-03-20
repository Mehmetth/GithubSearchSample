package com.mehmetpetek.githubsample.data.repository

import com.mehmetpetek.githubsample.data.remote.RetrofitDataSource
import com.mehmetpetek.githubsample.data.remote.model.Resource
import com.mehmetpetek.githubsample.data.remote.model.SearchResponse
import com.mehmetpetek.githubsample.data.remote.model.UserDetailResponse
import com.mehmetpetek.githubsample.domain.repository.GithubRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(private val retrofitDataSource: RetrofitDataSource) :
    GithubRepository {

    override fun search(
        query: String, page: String
    ): Flow<Resource<SearchResponse>> =
        callbackFlow {
            val response = retrofitDataSource.search(query, page)
            if (response.isSuccessful) {
                response.body()?.let {
                    trySend(Resource.Success(it))
                } ?: kotlin.run {
                    trySend(Resource.Fail(null))
                }
            } else {
                trySend(Resource.Error(null))
            }
            awaitClose { cancel() }
        }

    override fun userDetail(login: String): Flow<Resource<UserDetailResponse>> =
        callbackFlow {
            val response = retrofitDataSource.userDetail(login)
            if (response.isSuccessful) {
                response.body()?.let {
                    trySend(Resource.Success(it))
                } ?: kotlin.run {
                    trySend(Resource.Fail(null))
                }
            } else {
                trySend(Resource.Error(null))
            }
            awaitClose { cancel() }
        }
}
