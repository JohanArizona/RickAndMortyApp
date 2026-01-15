package com.takehomechallenge.arizona.domain.repository

import com.takehomechallenge.arizona.domain.model.Character
import com.takehomechallenge.arizona.domain.state.ResourceState
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(page: Int): Flow<ResourceState<List<Character>>>
    fun getCharacterDetail(id: Int): Flow<ResourceState<Character>>
    fun searchCharacters(query: String, page: Int): Flow<ResourceState<List<Character>>>
}