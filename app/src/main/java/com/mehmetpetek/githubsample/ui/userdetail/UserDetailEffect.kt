package com.mehmetpetek.githubsample.ui.userdetail

import com.mehmetpetek.githubsample.ui.base.Effect

sealed class UserDetailEffect : Effect {
    class ShowError(val throwable: Throwable?) : UserDetailEffect()
}