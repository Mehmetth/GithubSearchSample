package com.mehmetpetek.githubsample.app.di

import android.content.Context
import androidx.room.Room
import com.mehmetpetek.githubsample.BuildConfig
import com.mehmetpetek.githubsample.data.local.db.GithubUserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provide(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, GithubUserDatabase::class.java, BuildConfig.APPLICATION_ID + ".db"
    )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(db: GithubUserDatabase) = db.githubUserDao()

}