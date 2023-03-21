package com.mehmetpetek.githubsample.domain.repository

import com.mehmetpetek.githubsample.data.local.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface GithubUserDBRepository {
    suspend fun getAllUsers(): Flow<List<GithubUser>>

    suspend fun geGithubUsers(userId: Int): Flow<GithubUser?>

    suspend fun insertGithubUser(githubUser: GithubUser)

    suspend fun deleteGithubUser(userId: Int)
}