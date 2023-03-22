package com.mehmetpetek.githubsample.domain.repository

import com.mehmetpetek.githubsample.data.local.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface GithubUserDBRepository {
    fun getAllGithubUsers(): Flow<List<GithubUser>>

    fun getGithubUser(userId: Int): Flow<GithubUser?>

    suspend fun insertGithubUser(githubUser: GithubUser)

    suspend fun updateGithubUser(userId: Int)
}