package com.mehmetpetek.githubsample.ui.userdetail

import com.mehmetpetek.githubsample.ui.base.Event

sealed class UserDetailEvent : Event {
    object ClickFavorite : UserDetailEvent()
}