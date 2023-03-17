package com.mehmetpetek.githubsample.ui.userdetail

import com.mehmetpetek.githubsample.data.remote.model.ReposResponse
import com.mehmetpetek.githubsample.data.remote.model.UserDetailResponse
import com.mehmetpetek.githubsample.ui.base.State

data class UserDetailState(
    val isLoading: Boolean = false,
    val isFav: Boolean = false,
    val userDetailResponse: UserDetailResponse? = null,
    val repoList: List<ReposResponse>? = null
) : State