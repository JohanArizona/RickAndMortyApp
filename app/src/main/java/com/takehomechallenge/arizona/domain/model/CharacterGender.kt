package com.takehomechallenge.arizona.domain.model

enum class CharacterGender(val value: String) {
    Female("Female"),
    Male("Male"),
    Genderless("Genderless"),
    Unknown("unknown");

    companion object {
        fun fromString(value: String): CharacterGender {
            return entries.find { it.value.equals(value, ignoreCase = true) } ?: Unknown
        }
    }
}