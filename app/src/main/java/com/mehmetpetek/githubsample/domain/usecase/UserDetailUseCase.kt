package com.mehmetpetek.githubsample.domain.usecase

import com.mehmetpetek.githubsample.data.remote.model.Resource
import com.mehmetpetek.githubsample.data.remote.model.UserDetailResponse
import com.mehmetpetek.githubsample.domain.repository.GithubRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserDetailUseCase @Inject constructor(private val githubRepository: GithubRepository) {
    operator fun invoke(login: String): Flow<UserDetailState> = callbackFlow {
        githubRepository.userDetail(login).collect { result ->
            when (result) {
                is Resource.Success -> {
                    trySend(
                        UserDetailState.Success(result.data)
                    )
                }
                is Resource.Error,
                is Resource.Fail -> {
                    trySend(UserDetailState.Error(result.message))
                }
            }
        }
        awaitClose { cancel() }
    }

    sealed class UserDetailState {
        class Error(val throwable: Throwable?) : UserDetailState()
        class Success(val userDetailResponse: UserDetailResponse?) : UserDetailState()
    }
}