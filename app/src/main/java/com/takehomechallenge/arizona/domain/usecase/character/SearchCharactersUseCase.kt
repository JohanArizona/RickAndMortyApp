package com.takehomechallenge.arizona.domain.usecase.character

import com.takehomechallenge.arizona.domain.repository.SearchRepository
import javax.inject.Inject

class SearchCharactersUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(
        name: String,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null
    ) = repository.searchCharacters(name, status, species, type, gender, 1)

    fun getHistory() = repository.getSearchHistory()
    suspend fun addToHistory(query: String) = repository.addSearchHistory(query)
    suspend fun removeFromHistory(query: String) = repository.removeSearchHistory(query)
}