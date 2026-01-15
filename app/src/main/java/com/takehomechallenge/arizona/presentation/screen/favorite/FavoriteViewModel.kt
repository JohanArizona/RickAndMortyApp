package com.takehomechallenge.arizona.presentation.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takehomechallenge.arizona.domain.model.Character
import com.takehomechallenge.arizona.domain.usecase.favorite.GetFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Character>>(emptyList())
    val favorites: StateFlow<List<Character>> = _favorites.asStateFlow()

    init {
        getFavoritesUseCase().onEach { list ->
            _favorites.update { list }
        }.launchIn(viewModelScope)
    }
}