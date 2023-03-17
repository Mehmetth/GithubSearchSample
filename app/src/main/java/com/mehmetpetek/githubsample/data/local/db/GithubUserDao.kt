package com.mehmetpetek.githubsample.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mehmetpetek.githubsample.data.local.model.GithubUser

@Dao
interface GithubUserDao {
    @Query("SELECT * FROM githubuser ")
    suspend fun getAllUsers(): List<GithubUser>

    @Query("SELECT * FROM githubuser WHERE userId = :userId")
    suspend fun geGithubUsers(userId: Int): GithubUser?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGithubUser(githubUser: GithubUser)

    @Query("DELETE FROM githubuser WHERE userId = :userId")
    suspend fun deleteGithubUser(userId: Int)
}