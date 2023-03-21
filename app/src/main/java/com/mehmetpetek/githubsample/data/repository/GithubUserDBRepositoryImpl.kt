package com.mehmetpetek.githubsample.data.repository

import com.mehmetpetek.githubsample.data.local.db.GithubUserDao
import com.mehmetpetek.githubsample.data.local.model.GithubUser
import com.mehmetpetek.githubsample.domain.repository.GithubUserDBRepository
import javax.inject.Inject

class GithubUserDBRepositoryImpl @Inject constructor(
    private val dao: GithubUserDao,
) : GithubUserDBRepository {

    override fun getAllUsers() = dao.getAllUsers()
    override fun geGithubUsers(userId: Int) = dao.geGithubUsers(userId)
    override suspend fun insertGithubUser(githubUser: GithubUser) =
        dao.insertGithubUser(githubUser)

    override suspend fun deleteGithubUser(userId: Int) = dao.deleteGithubUser(userId)
}