package com.takehomechallenge.arizona.presentation.screen.search.state

import com.takehomechallenge.arizona.domain.model.Character

data class SearchUiState(
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    val error: String? = null,
    val isEmpty: Boolean = false
)