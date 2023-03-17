package com.mehmetpetek.githubsample.viewmodel

import com.mehmetpetek.githubsample.domain.repository.GithubUserDBRepository
import com.mehmetpetek.githubsample.domain.usecase.UsersUseCase
import com.mehmetpetek.githubsample.ui.home.HomeEvent
import com.mehmetpetek.githubsample.ui.home.HomeState
import com.mehmetpetek.githubsample.ui.home.HomeViewModel
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
class HomeViewModelTest {
    private lateinit var homeViewModel: HomeViewModel

    @Mock
    private lateinit var usersUseCase: UsersUseCase

    @Mock
    private lateinit var githubUserDBRepository: GithubUserDBRepository

    private val emittedEventList = mutableListOf(
        HomeEvent.SearchUser("Mehmetth"),
        HomeEvent.GoToUserDetail("Mehmetth"),
        HomeEvent.ClickFavorite(0, 123456),
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.IO)
        homeViewModel = HomeViewModel(usersUseCase, githubUserDBRepository)
    }

    @Test
    fun testState() = runTest {
        val emittedStateList = mutableListOf(
            HomeState(
                isLoading = false,
                searchQuery = "",
                pageNumber = 1,
                totalCount = null,
                usersList = null,
                isSame = false,
                changeIconIndex = 0,
            )
        )
        homeViewModel.setState {
            HomeState(
                isLoading = false,
                searchQuery = "",
                pageNumber = 1,
                totalCount = null,
                usersList = null,
                isSame = false,
                changeIconIndex = 0,
            )
        }
        val job = launch {
            homeViewModel.state.toList(emittedStateList)
        }

        Assert.assertEquals(HomeState(), homeViewModel.getCurrentState())
        job.cancel()
    }

    @Test
    fun testEventSearchUser() = runTest {
        homeViewModel.setEvent(HomeEvent.SearchUser("Mehmetth"))
        val job = launch { homeViewModel.event.toList(emittedEventList) }
        assert(emittedEventList[0] is HomeEvent.SearchUser)
        job.cancel()
    }

    @Test
    fun testEventGoToUserDetail() = runTest {
        homeViewModel.setEvent(HomeEvent.GoToUserDetail("Mehmetth"))
        val job = launch { homeViewModel.event.toList(emittedEventList) }
        assert(emittedEventList[1] is HomeEvent.GoToUserDetail)
        job.cancel()
    }

    @Test
    fun testEventClickFavorite() = runTest {
        homeViewModel.setEvent(HomeEvent.ClickFavorite(0, 123456))
        val job = launch { homeViewModel.event.toList(emittedEventList) }
        assert(emittedEventList[2] is HomeEvent.ClickFavorite)
        job.cancel()
    }
}