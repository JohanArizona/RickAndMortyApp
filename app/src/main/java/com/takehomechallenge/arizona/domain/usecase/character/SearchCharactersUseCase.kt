package com.takehomechallenge.arizona.domain.usecase.character

import com.takehomechallenge.arizona.domain.repository.CharacterRepository
import javax.inject.Inject

class SearchCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(query: String, page: Int) = repository.searchCharacters(query, page)
}