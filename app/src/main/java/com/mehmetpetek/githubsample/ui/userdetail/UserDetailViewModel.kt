package com.mehmetpetek.githubsample.ui.userdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mehmetpetek.githubsample.common.Constant
import com.mehmetpetek.githubsample.data.local.model.GithubUser
import com.mehmetpetek.githubsample.domain.repository.GithubUserDBRepository
import com.mehmetpetek.githubsample.domain.usecase.UserDetailUseCase
import com.mehmetpetek.githubsample.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userDetailUseCase: UserDetailUseCase,
    private val githubUserDBRepository: GithubUserDBRepository
) : BaseViewModel<UserDetailEvent, UserDetailState, UserDetailEffect>() {

    init {
        getUserDetail(savedStateHandle.get<String>(Constant.LOGIN) ?: "")
    }

    override fun setInitialState(): UserDetailState = UserDetailState(isLoading = true)

    override fun handleEvents(event: UserDetailEvent) {
        when (event) {
            is UserDetailEvent.ClickFavorite -> {
                clickFavorite()
            }
        }
    }

    private fun getUserDetail(login: String) {
        viewModelScope.launch {
            userDetailUseCase.invoke(login).collect {
                when (it) {
                    is UserDetailUseCase.UserDetailState.Success -> {
                        setState {
                            getCurrentState().copy(
                                isLoading = false,
                                userDetailResponse = it.userDetailResponse
                            )
                        }
                        setFavFavoriteIcon()
                    }
                    is UserDetailUseCase.UserDetailState.Error -> {
                        setState {
                            getCurrentState().copy(isLoading = false)
                        }
                        setEffect { UserDetailEffect.ShowError(it.throwable) }
                    }
                }
            }
        }
    }

    private fun setFavFavoriteIcon() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentState().userDetailResponse?.id?.let {
                if (githubUserDBRepository.geGithubUsers(it).first() == null) {
                    setState { getCurrentState().copy(isFav = false) }
                } else {
                    setState { getCurrentState().copy(isFav = true) }
                }
            }
        }
    }

    private fun clickFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentState().userDetailResponse?.id?.let {
                if (githubUserDBRepository.geGithubUsers(it).first() == null) {
                    setState { getCurrentState().copy(isFav = true) }
                    changeUserDB(true, it)
                } else {
                    setState { getCurrentState().copy(isFav = false) }
                    changeUserDB(false, it)
                }
            }
        }
    }

    private fun changeUserDB(value: Boolean, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (value) {
                githubUserDBRepository.insertGithubUser(GithubUser(userId = id))
            } else {
                githubUserDBRepository.deleteGithubUser(id)
            }
        }
    }
}