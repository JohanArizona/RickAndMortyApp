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
    private val useCase: SearchCharactersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadHistory()
    }

    private fun loadHistory() {
        useCase.getHistory().onEach { list ->
            _uiState.update { it.copy(history = list) }
        }.launchIn(viewModelScope)
    }

    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }

        searchJob?.cancel()

        if (newQuery.isBlank()) {

            _uiState.update { it.copy(characters = emptyList(), isEmpty = false, isLoading = false, error = null) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(500)
            executeSearch()
        }
    }

    fun applyFilter(status: String?, gender: String?, species: String, type: String) {
        _uiState.update {
            it.copy(
                filterStatus = status,
                filterGender = gender,
                filterSpecies = species,
                filterType = type
            )
        }

        if (_uiState.value.query.isNotBlank()) {
            executeSearch()
        }
    }

    fun clearFilter() {
        _uiState.update {
            it.copy(filterStatus = null, filterGender = null, filterSpecies = "", filterType = "")
        }
        if (_uiState.value.query.isNotBlank()) executeSearch()
    }

    fun deleteHistory(query: String) {
        viewModelScope.launch { useCase.removeFromHistory(query) }
    }

    fun onHistoryClicked(query: String) {
        onQueryChange(query)
    }

    private fun executeSearch() {
        val state = _uiState.value
        if (state.query.isBlank()) return

        viewModelScope.launch {
            useCase.addToHistory(state.query)
        }

        useCase(
            name = state.query,
            status = state.filterStatus,
            gender = state.filterGender,
            species = state.filterSpecies.ifBlank { null },
            type = state.filterType.ifBlank { null }
        ).onEach { result ->
            when (result) {
                is ResourceState.Loading -> _uiState.update { it.copy(isLoading = true, error = null, isEmpty = false) }
                is ResourceState.Success -> _uiState.update { it.copy(isLoading = false, characters = result.data, isEmpty = false) }
                is ResourceState.Empty -> _uiState.update { it.copy(isLoading = false, characters = emptyList(), isEmpty = true) }
                is ResourceState.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
            }
        }.launchIn(viewModelScope)
    }
}