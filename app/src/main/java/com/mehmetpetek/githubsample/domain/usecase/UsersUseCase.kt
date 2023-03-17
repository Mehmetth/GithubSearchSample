package com.mehmetpetek.githubsample.domain.usecase

import com.mehmetpetek.githubsample.data.remote.model.Resource
import com.mehmetpetek.githubsample.data.remote.model.SearchResponse
import com.mehmetpetek.githubsample.domain.repository.GithubRepository
import com.mehmetpetek.githubsample.domain.repository.GithubUserDBRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UsersUseCase @Inject constructor(
    private val githubRepository: GithubRepository,
    private val githubUserDBRepository: GithubUserDBRepository,
) {
    operator fun invoke(query: String): Flow<UsersState> =
        callbackFlow {
            val allUsers = githubUserDBRepository.getAllUsers()
            githubRepository.search(query).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.items?.forEach { user ->
                            user.favorite = allUsers.find { it.userId == user.id } != null
                        }
                        if (result.data?.items?.isEmpty() == true) {
                            trySend(
                                UsersState.NotFoundUsers
                            )
                        } else {
                            trySend(
                                UsersState.Success(result.data)
                            )
                        }
                    }
                    is Resource.Error,
                    is Resource.Fail -> {
                        trySend(UsersState.Error(result.message))
                    }
                }
            }
            awaitClose { cancel() }
        }

    sealed class UsersState {
        class Error(val throwable: Throwable?) : UsersState()
        class Success(val searchResponse: SearchResponse?) : UsersState()
        object NotFoundUsers : UsersState()
    }
}

