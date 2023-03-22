package com.mehmetpetek.githubsample.data.repository

import com.mehmetpetek.githubsample.data.local.db.GithubUserDao
import com.mehmetpetek.githubsample.data.local.model.GithubUser
import com.mehmetpetek.githubsample.domain.repository.GithubUserDBRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GithubUserDBRepositoryImpl @Inject constructor(
    private val dao: GithubUserDao,
) : GithubUserDBRepository {

    override fun getAllGithubUsers() = dao.getAllGithubUsers()
    override fun getGithubUser(userId: Int) = dao.getGithubUser(userId)
    override suspend fun insertGithubUser(githubUser: GithubUser) =
        dao.insertGithubUser(githubUser)

    override suspend fun updateGithubUser(userId: Int) {
        dao.getGithubUser(userId).first()?.let {
            it.isFav = !(it.isFav)
            dao.updateGithubUser(it)
        } ?: kotlin.run {
            dao.insertGithubUser(GithubUser(userId = userId, isFav = true))
        }
    }
}