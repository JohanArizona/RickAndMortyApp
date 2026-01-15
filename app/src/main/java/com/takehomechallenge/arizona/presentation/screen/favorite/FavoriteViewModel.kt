package com.takehomechallenge.arizona.presentation.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takehomechallenge.arizona.domain.model.Character
import com.takehomechallenge.arizona.domain.usecase.favorite.GetFavoritesUseCase
import com.takehomechallenge.arizona.domain.usecase.favorite.RemoveFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Character>>(emptyList())
    val favorites: StateFlow<List<Character>> = _favorites.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        getFavoritesUseCase().onEach { list ->
            _favorites.update { list }
        }.launchIn(viewModelScope)
    }

    fun removeFromFavorites(character: Character) {
        viewModelScope.launch {
            removeFavoriteUseCase(character.id)
        }
    }
}