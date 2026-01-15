package com.takehomechallenge.arizona.domain.usecase.character

import com.takehomechallenge.arizona.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(page: Int) = repository.getCharacters(page)
}