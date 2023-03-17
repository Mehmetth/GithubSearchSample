package com.mehmetpetek.githubsample.app.di

import com.mehmetpetek.githubsample.data.remote.GithubService
import com.mehmetpetek.githubsample.data.remote.RetrofitDataSource
import com.mehmetpetek.githubsample.data.repository.GithubRepositoryImpl
import com.mehmetpetek.githubsample.domain.repository.GithubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRetrofitRepository(
        retrofitDataSource: RetrofitDataSource,
    ): GithubRepository = GithubRepositoryImpl(retrofitDataSource)

    @Provides
    @Singleton
    fun provideRetrofitDataSource(
        githubService: GithubService,
    ): RetrofitDataSource = RetrofitDataSource(githubService)
}