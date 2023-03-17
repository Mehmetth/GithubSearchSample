package com.mehmetpetek.githubsample.ui.home

import com.mehmetpetek.githubsample.ui.base.Event

sealed class HomeEvent : Event {
    class SearchUser(val userName: String) : HomeEvent()
    class GoToUserDetail(val login: String) : HomeEvent()
    class ClickFavorite(val index: Int, val id: Int) : HomeEvent()
}
