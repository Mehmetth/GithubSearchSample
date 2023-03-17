package com.mehmetpetek.githubsample.domain.repository

import com.mehmetpetek.githubsample.data.local.db.GithubUserDao
import com.mehmetpetek.githubsample.data.local.model.GithubUser
import javax.inject.Inject

class GithubUserDBRepository @Inject constructor(
    private val dao: GithubUserDao,
) {
    suspend fun getAllUsers() = dao.getAllUsers()

    suspend fun insertGithubUser(githubUser: GithubUser) =
        dao.insertGithubUser(githubUser)

    suspend fun getGithubUser(userId: Int): GithubUser? = dao.geGithubUsers(userId)

    suspend fun deleteGithubUser(userId: Int) = dao.deleteGithubUser(userId)
}