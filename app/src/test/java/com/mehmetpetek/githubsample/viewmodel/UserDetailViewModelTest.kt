package com.mehmetpetek.githubsample.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.mehmetpetek.githubsample.domain.repository.GithubUserDBRepository
import com.mehmetpetek.githubsample.domain.usecase.UserDetailUseCase
import com.mehmetpetek.githubsample.ui.userdetail.UserDetailEvent
import com.mehmetpetek.githubsample.ui.userdetail.UserDetailState
import com.mehmetpetek.githubsample.ui.userdetail.UserDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailViewModelTest {
    private lateinit var userDetailViewModel: UserDetailViewModel

    @Mock
    private lateinit var savedStateHandle: SavedStateHandle

    @Mock
    private lateinit var userDetailUseCase: UserDetailUseCase

    @Mock
    private lateinit var githubUserDBRepository: GithubUserDBRepository

    private val emittedEventList = mutableListOf(
        UserDetailEvent.ClickFavorite
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.IO)
        userDetailViewModel =
            UserDetailViewModel(savedStateHandle, userDetailUseCase, githubUserDBRepository)
    }

    @Test
    fun testState() = runTest {
        val emittedStateList = mutableListOf(
            UserDetailState(
                isLoading = false,
                isFav = false,
                userDetailResponse = null,
                repoList = null
            )
        )
        userDetailViewModel.setState {
            UserDetailState(
                isLoading = false,
                isFav = false,
                userDetailResponse = null,
                repoList = null
            )
        }
        val job = launch {
            userDetailViewModel.state.toList(emittedStateList)
        }

        Assert.assertEquals(UserDetailState(), userDetailViewModel.getCurrentState())
        job.cancel()
    }

    @Test
    fun testEventSearchUser() = runTest {
        userDetailViewModel.setEvent(UserDetailEvent.ClickFavorite)
        val job = launch { userDetailViewModel.event.toList(emittedEventList.toMutableList()) }
        assert(emittedEventList[0] is UserDetailEvent.ClickFavorite)
        job.cancel()
    }
}