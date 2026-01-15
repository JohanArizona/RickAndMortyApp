package com.takehomechallenge.arizona.presentation.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takehomechallenge.arizona.domain.model.Character
import com.takehomechallenge.arizona.domain.state.ResourceState
import com.takehomechallenge.arizona.domain.usecase.character.GetCharacterDetailUseCase
import com.takehomechallenge.arizona.domain.usecase.character.GetCharactersUseCase
import com.takehomechallenge.arizona.domain.usecase.favorite.AddFavoriteUseCase
import com.takehomechallenge.arizona.domain.usecase.favorite.CheckFavoriteUseCase
import com.takehomechallenge.arizona.domain.usecase.favorite.RemoveFavoriteUseCase
import com.takehomechallenge.arizona.presentation.screen.detail.state.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCharacterDetailUseCase: GetCharacterDetailUseCase,
    private val getCharactersUseCase: GetCharactersUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val checkFavoriteUseCase: CheckFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun getCharacterDetail(id: Int) {
        checkFavoriteUseCase(id).onEach { isFav ->
            _uiState.update { it.copy(isFavorite = isFav) }
        }.launchIn(viewModelScope)

        getCharacterDetailUseCase(id).onEach { result ->
            when (result) {
                is ResourceState.Loading -> _uiState.update { it.copy(isLoading = true) }
                is ResourceState.Success -> {
                    _uiState.update { it.copy(isLoading = false, character = result.data) }
                    loadRecommendations()
                }
                is ResourceState.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    private fun loadRecommendations() {
        val randomPage = Random.nextInt(1, 42)
        getCharactersUseCase(randomPage).onEach { result ->
            if (result is ResourceState.Success) {
                _uiState.update { it.copy(recommendations = result.data) }
            }
        }.launchIn(viewModelScope)
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            if (_uiState.value.isFavorite) {
                removeFavoriteUseCase(character.id)
            } else {
                addFavoriteUseCase(character)
            }
        }
    }
}