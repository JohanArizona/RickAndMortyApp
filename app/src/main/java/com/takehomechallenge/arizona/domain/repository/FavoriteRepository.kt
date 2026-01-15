package com.takehomechallenge.arizona.domain.repository

import com.takehomechallenge.arizona.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getAllFavorites(): Flow<List<Character>>
    fun checkFavoriteStatus(id: Int): Flow<Boolean>
    suspend fun addFavorite(character: Character)
    suspend fun removeFavorite(id: Int)
}