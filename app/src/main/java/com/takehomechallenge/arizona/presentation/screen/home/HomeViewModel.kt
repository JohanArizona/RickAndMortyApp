package com.takehomechallenge.arizona.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takehomechallenge.arizona.domain.model.Character
import com.takehomechallenge.arizona.domain.state.ResourceState
import com.takehomechallenge.arizona.domain.usecase.character.GetCharactersUseCase
import com.takehomechallenge.arizona.domain.usecase.favorite.AddFavoriteUseCase
import com.takehomechallenge.arizona.domain.usecase.favorite.GetFavoritesUseCase
import com.takehomechallenge.arizona.domain.usecase.favorite.RemoveFavoriteUseCase
import com.takehomechallenge.arizona.presentation.screen.home.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var favoriteIds = setOf<Int>()

    init {
        getFavoritesUseCase().onEach { favorites ->
            favoriteIds = favorites.map { it.id }.toSet()
            updateCharacterListWithFavorites()
        }.launchIn(viewModelScope)

        loadCharacters()
    }

    fun loadNextPage() {
        if (!_uiState.value.isLoading) {
            loadCharacters()
        }
    }

    private fun loadCharacters() {
        val currentPage = _uiState.value.page

        getCharactersUseCase(currentPage).onEach { result ->
            when (result) {
                is ResourceState.Loading -> {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                }
                is ResourceState.Success -> {
                    val newCharacters = result.data.map { char ->
                        char.copy(isFavorite = favoriteIds.contains(char.id))
                    }

                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            characters = currentState.characters + newCharacters,
                            error = null,
                            page = currentState.page + 1
                        )
                    }
                }
                is ResourceState.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is ResourceState.Empty -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun updateCharacterListWithFavorites() {
        val currentList = _uiState.value.characters
        if (currentList.isNotEmpty()) {
            val updatedList = currentList.map { char ->
                char.copy(isFavorite = favoriteIds.contains(char.id))
            }
            _uiState.update { it.copy(characters = updatedList) }
        }
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            if (favoriteIds.contains(character.id)) {
                removeFavoriteUseCase(character.id)
            } else {
                addFavoriteUseCase(character.copy(isFavorite = true))
            }
        }
    }
}