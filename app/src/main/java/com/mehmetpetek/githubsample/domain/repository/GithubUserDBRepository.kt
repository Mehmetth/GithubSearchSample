package com.mehmetpetek.githubsample.domain.repository

import com.mehmetpetek.githubsample.data.local.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface GithubUserDBRepository {
    fun getAllUsers(): Flow<List<GithubUser>>

    fun geGithubUsers(userId: Int): Flow<GithubUser?>

    suspend fun insertGithubUser(githubUser: GithubUser)

    suspend fun deleteGithubUser(userId: Int)
}