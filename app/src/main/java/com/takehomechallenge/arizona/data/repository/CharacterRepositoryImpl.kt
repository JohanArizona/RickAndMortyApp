package com.takehomechallenge.arizona.data.repository

import com.takehomechallenge.arizona.data.local.dao.CharacterDao
import com.takehomechallenge.arizona.data.remote.api.RickMortyApi
import com.takehomechallenge.arizona.data.repository.mapper.CharacterMapper
import com.takehomechallenge.arizona.domain.model.Character
import com.takehomechallenge.arizona.domain.repository.CharacterRepository
import com.takehomechallenge.arizona.domain.state.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: RickMortyApi,
    private val dao: CharacterDao
) : CharacterRepository {

    override fun getCharacters(page: Int): Flow<ResourceState<List<Character>>> = flow {
        emit(ResourceState.Loading)

        try {
            val localData = dao.getCharactersByPage(page)
            if (localData.isNotEmpty()) {
                emit(ResourceState.Success(localData.map { CharacterMapper.mapEntityToDomain(it) }))
            }

            val response = api.getCharacters(page)
            val entities = response.results.map { CharacterMapper.mapDtoToEntity(it, page) }

            dao.insertAll(entities)

            val updatedLocalData = dao.getCharactersByPage(page)
            emit(ResourceState.Success(updatedLocalData.map { CharacterMapper.mapEntityToDomain(it) }))

        } catch (e: HttpException) {
            emit(ResourceState.Error("Server error: ${e.code()}"))
        } catch (e: IOException) {
            val localData = dao.getCharactersByPage(page)
            if (localData.isNotEmpty()) {
                emit(ResourceState.Success(localData.map { CharacterMapper.mapEntityToDomain(it) }))
            } else {
                emit(ResourceState.Error("No internet connection and no cached data"))
            }
        } catch (e: Exception) {
            emit(ResourceState.Error("Unknown error: ${e.localizedMessage}"))
        }
    }

    override fun getCharacterDetail(id: Int): Flow<ResourceState<Character>> = flow {
        emit(ResourceState.Loading)
        try {
            val cached = dao.getCharacterById(id)
            if (cached != null) {
                emit(ResourceState.Success(CharacterMapper.mapEntityToDomain(cached)))
            }

            val response = api.getCharacterDetail(id)
            emit(ResourceState.Success(CharacterMapper.mapDtoToDomain(response)))
        } catch (e: Exception) {
            val cached = dao.getCharacterById(id)
            if (cached != null) {
                emit(ResourceState.Success(CharacterMapper.mapEntityToDomain(cached)))
            } else {
                emit(ResourceState.Error("Failed to load detail: ${e.localizedMessage}"))
            }
        }
    }

    override fun searchCharacters(query: String, page: Int): Flow<ResourceState<List<Character>>> = flow {
        emit(ResourceState.Loading)
        try {
            val response = api.searchCharacters(
                name = query,
                status = null,
                species = null,
                type = null,
                gender = null,
                page = page
            )

            if (response.results.isEmpty()) {
                emit(ResourceState.Empty)
            } else {
                val domainData = response.results.map { CharacterMapper.mapDtoToDomain(it) }
                emit(ResourceState.Success(domainData))
            }
        } catch (e: HttpException) {
            if (e.code() == 404) {
                emit(ResourceState.Empty)
            } else {
                emit(ResourceState.Error("Search error: ${e.code()}"))
            }
        } catch (e: IOException) {
            emit(ResourceState.Error("No internet connection"))
        }
    }
}