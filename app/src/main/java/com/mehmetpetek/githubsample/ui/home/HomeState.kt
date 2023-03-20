package com.mehmetpetek.githubsample.ui.home

import com.mehmetpetek.githubsample.data.remote.model.Users
import com.mehmetpetek.githubsample.ui.base.State

data class HomeState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val totalCount: Int? = null,
    val usersList: List<Users>? = null,
    val isSame: Boolean = false,
    val changeIconIndex: Int = -1,
    val page: Int = 1,
) : State