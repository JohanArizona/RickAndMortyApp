package com.takehomechallenge.arizona.domain.usecase.character

import com.takehomechallenge.arizona.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterDetailUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(id: Int) = repository.getCharacterDetail(id)
}