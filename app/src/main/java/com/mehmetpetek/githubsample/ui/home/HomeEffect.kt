package com.mehmetpetek.githubsample.ui.home

import com.mehmetpetek.githubsample.ui.base.Effect

sealed class HomeEffect : Effect {
    class ShowError(val throwable: Throwable?) : HomeEffect()
    class GoToUserDetail(val login: String) : HomeEffect()
}