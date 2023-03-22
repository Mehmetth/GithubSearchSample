package com.mehmetpetek.githubsample.domain.usecase

import com.mehmetpetek.githubsample.data.remote.model.Resource
import com.mehmetpetek.githubsample.data.remote.model.SearchResponse
import com.mehmetpetek.githubsample.domain.repository.GithubRepository
import com.mehmetpetek.githubsample.domain.repository.GithubUserDBRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class UsersUseCase @Inject constructor(
    private val githubRepository: GithubRepository,
    private val githubUserDBRepository: GithubUserDBRepository,
) {
    operator fun invoke(query: String, page: String): Flow<UsersState> =
        callbackFlow {
            val users = githubRepository.search(query, page)
            val dbUsers = githubUserDBRepository.getAllGithubUsers()

            users.combine(dbUsers) { _users, _dbUsers ->
                when (_users) {
                    is Resource.Success -> {
                        _users.data?.items?.forEach { user ->
                            user.favorite = _dbUsers.find { it.userId == user.id }?.let {
                                it.isFav
                            } ?: kotlin.run { false }
                        }
                        if (_users.data?.items?.isEmpty() == true) {
                            UsersState.NotFoundUsers

                        } else {
                            UsersState.Success(_users.data)
                        }
                    }
                    is Resource.Error,
                    is Resource.Fail -> {
                        UsersState.Error(_users.message)
                    }
                }
            }.collect {
                trySend(it)
            }
            awaitClose { cancel() }
        }

    sealed class UsersState {
        class Error(val throwable: Throwable?) : UsersState()
        class Success(val searchResponse: SearchResponse?) : UsersState()
        object NotFoundUsers : UsersState()
    }
}

