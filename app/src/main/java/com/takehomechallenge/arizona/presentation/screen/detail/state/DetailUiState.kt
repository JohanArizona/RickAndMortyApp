package com.takehomechallenge.arizona.presentation.screen.detail.state

import com.takehomechallenge.arizona.domain.model.Character

data class DetailUiState(
    val isLoading: Boolean = false,
    val character: Character? = null,
    val recommendations: List<Character> = emptyList(),
    val isFavorite: Boolean = false,
    val error: String? = null
)