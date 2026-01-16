package com.takehomechallenge.arizona.data

import com.google.common.truth.Truth.assertThat
import com.takehomechallenge.arizona.data.local.entity.CharacterEntity
import com.takehomechallenge.arizona.data.repository.mapper.CharacterMapper
import com.takehomechallenge.arizona.domain.model.CharacterGender
import com.takehomechallenge.arizona.domain.model.CharacterStatus
import org.junit.Test

class MapperTest {

    @Test
    fun `mapEntityToDomain converts data correctly`() {
        // Given
        val entity = CharacterEntity(
            id = 1,
            name = "Morty",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            originName = "Earth",
            locationName = "Earth",
            image = "img.jpg",
            episode = listOf("1"),
            url = "url",
            page = 1
        )

        // When
        val domain = CharacterMapper.mapEntityToDomain(entity, isFavorite = true)

        // Then
        assertThat(domain.name).isEqualTo("Morty")
        assertThat(domain.status).isEqualTo(CharacterStatus.Alive)
        assertThat(domain.gender).isEqualTo(CharacterGender.Male)
        assertThat(domain.isFavorite).isTrue()
    }

    @Test
    fun `mapDtoToDomain handles unknown status gracefully`() {
        val statusString = "Mati Suri"
        val result = CharacterStatus.fromString(statusString)

        assertThat(result).isEqualTo(CharacterStatus.Unknown)
    }
}