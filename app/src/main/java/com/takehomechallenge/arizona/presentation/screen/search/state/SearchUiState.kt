package com.takehomechallenge.arizona.presentation.screen.search.state

import com.takehomechallenge.arizona.domain.model.Character

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    val error: String? = null,
    val isEmpty: Boolean = false,
    val history: List<String> = emptyList(),

    val filterStatus: String? = null,
    val filterGender: String? = null,
    val filterSpecies: String = "",
    val filterType: String = ""
)