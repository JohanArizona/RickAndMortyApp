package com.takehomechallenge.arizona.data.repository.mapper

import com.takehomechallenge.arizona.data.local.entity.CharacterEntity
import com.takehomechallenge.arizona.data.local.entity.FavoriteEntity
import com.takehomechallenge.arizona.data.remote.dto.model.CharacterDto
import com.takehomechallenge.arizona.domain.model.Character
import com.takehomechallenge.arizona.domain.model.CharacterGender
import com.takehomechallenge.arizona.domain.model.CharacterStatus
import com.takehomechallenge.arizona.data.local.database.DatabaseConverters

object CharacterMapper {
    private val converter = DatabaseConverters()

    fun mapDtoToEntity(dto: CharacterDto, page: Int): CharacterEntity {
        return CharacterEntity(
            id = dto.id,
            name = dto.name,
            status = dto.status,
            species = dto.species,
            type = dto.type,
            gender = dto.gender,
            originName = dto.origin.name,
            locationName = dto.location.name,
            image = dto.image,
            episode = dto.episode,
            url = dto.url,
            page = page
        )
    }

    fun mapEntityToDomain(entity: CharacterEntity, isFavorite: Boolean = false): Character {
        return Character(
            id = entity.id,
            name = entity.name,
            status = CharacterStatus.fromString(entity.status),
            species = entity.species,
            type = entity.type,
            gender = CharacterGender.fromString(entity.gender),
            originName = entity.originName,
            locationName = entity.locationName,
            image = entity.image,
            episode = entity.episode,
            url = entity.url,
            isFavorite = isFavorite
        )
    }

    fun mapDtoToDomain(dto: CharacterDto): Character {
        return Character(
            id = dto.id,
            name = dto.name,
            status = CharacterStatus.fromString(dto.status),
            species = dto.species,
            type = dto.type,
            gender = CharacterGender.fromString(dto.gender),
            originName = dto.origin.name,
            locationName = dto.location.name,
            image = dto.image,
            episode = dto.episode,
            url = dto.url,
            isFavorite = false
        )
    }

    fun mapDomainToFavoriteEntity(domain: Character): FavoriteEntity {
        return FavoriteEntity(
            id = domain.id,
            name = domain.name,
            status = domain.status.value,
            species = domain.species,
            gender = domain.gender.value,
            originName = domain.originName,
            locationName = domain.locationName,
            image = domain.image
        )
    }

    fun mapFavoriteEntityToDomain(entity: FavoriteEntity): Character {
        return Character(
            id = entity.id,
            name = entity.name,
            status = CharacterStatus.fromString(entity.status),
            species = entity.species,
            type = "",
            gender = CharacterGender.fromString(entity.gender),
            originName = entity.originName,
            locationName = entity.locationName,
            image = entity.image,
            episode = emptyList(),
            url = "",
            isFavorite = true
        )
    }
}