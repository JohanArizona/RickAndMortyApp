package com.takehomechallenge.arizona.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takehomechallenge.arizona.domain.state.ResourceState
import com.takehomechallenge.arizona.domain.usecase.character.SearchCharactersUseCase
import com.takehomechallenge.arizona.presentation.screen.search.state.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCharactersUseCase: SearchCharactersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) {
            _uiState.update { SearchUiState() } // Reset
            return
        }

        // Debounce manual 500ms
        searchJob = viewModelScope.launch {
            delay(500)
            searchCharacters(query)
        }
    }

    private fun searchCharacters(query: String) {
        searchCharactersUseCase(query, 1).onEach { result ->
            when (result) {
                is ResourceState.Loading -> _uiState.update { it.copy(isLoading = true, error = null, isEmpty = false) }
                is ResourceState.Success -> _uiState.update { it.copy(isLoading = false, characters = result.data, isEmpty = false) }
                is ResourceState.Empty -> _uiState.update { it.copy(isLoading = false, characters = emptyList(), isEmpty = true) }
                is ResourceState.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
            }
        }.launchIn(viewModelScope)
    }
}