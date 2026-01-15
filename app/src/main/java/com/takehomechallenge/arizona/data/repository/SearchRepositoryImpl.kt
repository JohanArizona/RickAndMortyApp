package com.takehomechallenge.arizona.data.repository

import com.takehomechallenge.arizona.data.local.dao.SearchHistoryDao
import com.takehomechallenge.arizona.data.local.entity.SearchHistoryEntity
import com.takehomechallenge.arizona.data.remote.api.RickMortyApi
import com.takehomechallenge.arizona.data.repository.mapper.CharacterMapper
import com.takehomechallenge.arizona.domain.model.Character
import com.takehomechallenge.arizona.domain.repository.SearchRepository
import com.takehomechallenge.arizona.domain.state.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: RickMortyApi,
    private val historyDao: SearchHistoryDao
) : SearchRepository {

    override fun searchCharacters(
        name: String,
        status: String?,
        species: String?,
        type: String?,
        gender: String?,
        page: Int
    ): Flow<ResourceState<List<Character>>> = flow {
        emit(ResourceState.Loading)
        try {
            // Panggil API dengan semua parameter filter
            val response = api.searchCharacters(name, status, species, type, gender, page)
            if (response.results.isEmpty()) {
                emit(ResourceState.Empty)
            } else {
                val data = response.results.map { CharacterMapper.mapDtoToDomain(it) }
                emit(ResourceState.Success(data))
            }
        } catch (e: HttpException) {
            if (e.code() == 404) emit(ResourceState.Empty)
            else emit(ResourceState.Error("Error: ${e.code()}"))
        } catch (e: IOException) {
            emit(ResourceState.Error("No internet connection"))
        } catch (e: Exception) {
            emit(ResourceState.Error(e.localizedMessage ?: "Unknown error"))
        }
    }

    override fun getSearchHistory(): Flow<List<String>> {
        return historyDao.getRecentSearches().map { list -> list.map { it.query } }
    }

    override suspend fun addSearchHistory(query: String) {
        historyDao.insertQuery(SearchHistoryEntity(query = query))
    }

    override suspend fun removeSearchHistory(query: String) {
        historyDao.deleteQuery(query)
    }
}