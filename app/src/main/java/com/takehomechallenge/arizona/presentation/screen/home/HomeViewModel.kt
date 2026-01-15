package com.takehomechallenge.arizona.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takehomechallenge.arizona.domain.state.ResourceState
import com.takehomechallenge.arizona.domain.usecase.character.GetCharactersUseCase
import com.takehomechallenge.arizona.presentation.screen.home.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        val currentPage = _uiState.value.page
        getCharactersUseCase(currentPage).onEach { result ->
            when (result) {
                is ResourceState.Loading -> {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                }
                is ResourceState.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            characters = result.data,
                            error = null
                        )
                    }
                }
                is ResourceState.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is ResourceState.Empty -> {
                    _uiState.update { it.copy(isLoading = false, characters = emptyList()) }
                }
            }
        }.launchIn(viewModelScope)
    }
}