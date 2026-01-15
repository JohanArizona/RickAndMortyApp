package com.takehomechallenge.arizona.data.repository

import com.takehomechallenge.arizona.data.local.dao.FavoriteDao
import com.takehomechallenge.arizona.data.repository.mapper.CharacterMapper
import com.takehomechallenge.arizona.domain.model.Character
import com.takehomechallenge.arizona.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao
) : FavoriteRepository {

    override fun getAllFavorites(): Flow<List<Character>> {
        return dao.getAllFavorites().map { entities ->
            entities.map { CharacterMapper.mapFavoriteEntityToDomain(it) }
        }
    }

    override fun checkFavoriteStatus(id: Int): Flow<Boolean> {
        return dao.isFavorite(id)
    }

    override suspend fun addFavorite(character: Character) {
        val entity = CharacterMapper.mapDomainToFavoriteEntity(character)
        dao.insertFavorite(entity)
    }

    override suspend fun removeFavorite(id: Int) {
        dao.deleteFavoriteById(id)
    }
}