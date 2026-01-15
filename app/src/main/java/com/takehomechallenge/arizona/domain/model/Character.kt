package com.takehomechallenge.arizona.domain.model

data class Character(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: CharacterGender,
    val originName: String,
    val locationName: String,
    val image: String,
    val episode: List<String>,
    val url: String,
    val isFavorite: Boolean = false
)