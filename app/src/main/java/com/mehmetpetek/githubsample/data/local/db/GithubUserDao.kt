package com.mehmetpetek.githubsample.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mehmetpetek.githubsample.data.local.model.GithubUser
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubUserDao {
    @Query("SELECT * FROM githubuser ")
    fun getAllUsers(): Flow<List<GithubUser>>

    @Query("SELECT * FROM githubuser WHERE userId = :userId")
    fun geGithubUsers(userId: Int): Flow<GithubUser?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGithubUser(githubUser: GithubUser)

    @Query("DELETE FROM githubuser WHERE userId = :userId")
    fun deleteGithubUser(userId: Int)
}