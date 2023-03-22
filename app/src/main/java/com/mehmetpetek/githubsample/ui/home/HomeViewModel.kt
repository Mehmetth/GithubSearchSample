package com.mehmetpetek.githubsample.ui.home

import androidx.lifecycle.viewModelScope
import com.mehmetpetek.githubsample.domain.repository.GithubUserDBRepository
import com.mehmetpetek.githubsample.domain.usecase.UsersUseCase
import com.mehmetpetek.githubsample.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val usersUseCase: UsersUseCase,
    private val githubUserDBRepository: GithubUserDBRepository
) : BaseViewModel<HomeEvent, HomeState, HomeEffect>() {

    override fun setInitialState(): HomeState = HomeState(isLoading = false)

    override fun handleEvents(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchUser -> {
                //If the user goes to the detail page and comes back, the request is made to continue from where it left off.
                if (getCurrentState().searchQuery != event.userName)
                    getUsers(event.userName, 1)
            }
            is HomeEvent.GoToUserDetail -> {
                setEffect { HomeEffect.GoToUserDetail(event.login) }
            }
            is HomeEvent.ClickFavorite -> {
                changeFavoriteIcon(event.index, event.id)
            }
            is HomeEvent.LoadMore -> {
                getUsers(getCurrentState().searchQuery, getCurrentState().page + 1)
            }
        }
    }

    private fun getUsers(userName: String, page: Int) {
        setState { getCurrentState().copy(isLoading = true) }
        viewModelScope.launch {
            usersUseCase.invoke(userName, page = page.toString()).collect {
                when (it) {
                    is UsersUseCase.UsersState.Success -> {
                        setState {
                            getCurrentState().copy(
                                isLoading = false,
                                totalCount = it.searchResponse?.total_count,
                                usersList = it.searchResponse?.items,
                                searchQuery = userName,
                                page = page,
                                changeIconIndex = -1
                            )
                        }
                    }
                    is UsersUseCase.UsersState.NotFoundUsers -> {
                        setState {
                            getCurrentState().copy(
                                isLoading = false,
                                usersList = emptyList(),
                                changeIconIndex = -1
                            )
                        }
                    }
                    is UsersUseCase.UsersState.Error -> {
                        setState {
                            getCurrentState().copy(
                                isLoading = false
                            )
                        }
                        setEffect { HomeEffect.ShowError(it.throwable) }
                    }
                }
            }
        }
    }

    private fun changeFavoriteIcon(index: Int, id: Int) {
        changeDBFavorite(id)
        setState {
            getCurrentState().copy(
                isSame = !(getCurrentState().isSame),
                usersList = getCurrentState().usersList,
                changeIconIndex = index
            )
        }
    }

    private fun changeDBFavorite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            githubUserDBRepository.updateGithubUser(id)
        }
    }
}