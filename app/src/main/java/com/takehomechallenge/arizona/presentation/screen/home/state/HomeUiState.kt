package com.takehomechallenge.arizona.presentation.screen.home.state

import com.takehomechallenge.arizona.domain.model.Character

data class HomeUiState(
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    val error: String? = null,
    val page: Int = 1
)