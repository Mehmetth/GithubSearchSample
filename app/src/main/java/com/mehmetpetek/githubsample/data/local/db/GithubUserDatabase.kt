package com.mehmetpetek.githubsample.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mehmetpetek.githubsample.data.local.model.GithubUser

@Database(entities = [GithubUser::class], version = 1)
abstract class GithubUserDatabase : RoomDatabase() {
    abstract fun githubUserDao(): GithubUserDao
}