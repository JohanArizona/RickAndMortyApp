package com.takehomechallenge.arizona.domain.model

enum class CharacterStatus(val value: String) {
    Alive("Alive"),
    Dead("Dead"),
    Unknown("unknown");

    companion object {
        fun fromString(value: String): CharacterStatus {
            return entries.find { it.value.equals(value, ignoreCase = true) } ?: Unknown
        }
    }
}