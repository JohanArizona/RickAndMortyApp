package com.takehomechallenge.arizona.di

import com.takehomechallenge.arizona.data.repository.CharacterRepositoryImpl
import com.takehomechallenge.arizona.data.repository.FavoriteRepositoryImpl
import com.takehomechallenge.arizona.domain.repository.CharacterRepository
import com.takehomechallenge.arizona.domain.repository.FavoriteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCharacterRepository(
        characterRepositoryImpl: CharacterRepositoryImpl
    ): CharacterRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository
}