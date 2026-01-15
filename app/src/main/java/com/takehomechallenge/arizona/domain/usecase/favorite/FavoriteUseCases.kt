package com.takehomechallenge.arizona.domain.usecase.favorite

import com.takehomechallenge.arizona.domain.model.Character
import com.takehomechallenge.arizona.domain.repository.FavoriteRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository) {
    suspend operator fun invoke(character: Character) = repository.addFavorite(character)
}

class RemoveFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository) {
    suspend operator fun invoke(id: Int) = repository.removeFavorite(id)
}

class CheckFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository) {
    operator fun invoke(id: Int) = repository.checkFavoriteStatus(id)
}

class GetFavoritesUseCase @Inject constructor(private val repository: FavoriteRepository) {
    operator fun invoke() = repository.getAllFavorites()
}