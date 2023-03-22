package com.mehmetpetek.githubsample.data.local.db

import androidx.room.*
import com.mehmetpetek.githubsample.data.local.model.GithubUser
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubUserDao {
    @Query("SELECT * FROM githubuser ")
    fun getAllGithubUsers(): Flow<List<GithubUser>>

    @Query("SELECT * FROM githubuser WHERE userId = :userId")
    fun getGithubUser(userId: Int): Flow<GithubUser?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGithubUser(githubUser: GithubUser)

    @Update
    fun updateGithubUser(githubUser: GithubUser)
}