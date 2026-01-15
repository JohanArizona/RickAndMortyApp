package com.takehomechallenge.arizona.domain.repository

import com.takehomechallenge.arizona.domain.model.Character
import com.takehomechallenge.arizona.domain.state.ResourceState
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchCharacters(
        name: String,
        status: String?,
        species: String?,
        type: String?,
        gender: String?,
        page: Int
    ): Flow<ResourceState<List<Character>>>

    fun getSearchHistory(): Flow<List<String>>
    suspend fun addSearchHistory(query: String)
    suspend fun removeSearchHistory(query: String)
}