package com.takehomechallenge.arizona.domain

import com.google.common.truth.Truth.assertThat
import com.takehomechallenge.arizona.domain.model.Character
import com.takehomechallenge.arizona.domain.repository.CharacterRepository
import com.takehomechallenge.arizona.domain.repository.FavoriteRepository
import com.takehomechallenge.arizona.domain.repository.SearchRepository
import com.takehomechallenge.arizona.domain.state.ResourceState
import com.takehomechallenge.arizona.domain.usecase.character.GetCharactersUseCase
import com.takehomechallenge.arizona.domain.usecase.character.SearchCharactersUseCase
import com.takehomechallenge.arizona.domain.usecase.favorite.AddFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UseCaseTest {

    private val characterRepo = mockk<CharacterRepository>()
    private val searchRepo = mockk<SearchRepository>()
    private val favoriteRepo = mockk<FavoriteRepository>(relaxed = true)

    @Test
    fun `GetCharactersUseCase should return Success with data`() = runTest {
        // Given
        val dummyData = listOf(mockk<Character>())
        coEvery { characterRepo.getCharacters(1) } returns flowOf(ResourceState.Success(dummyData))
        val useCase = GetCharactersUseCase(characterRepo)

        // When
        val result = useCase(1).first()

        // Then
        assertThat(result).isInstanceOf(ResourceState.Success::class.java)
        assertThat((result as ResourceState.Success).data).hasSize(1)
    }

    @Test
    fun `SearchCharactersUseCase should return Empty when no result found`() = runTest {
        // Given
        val query = "Zorglub"
        coEvery { searchRepo.searchCharacters(query, null, null, null, null, 1) } returns flowOf(ResourceState.Empty)
        val useCase = SearchCharactersUseCase(searchRepo)

        // When
        val result = useCase(query).first()

        // Then
        assertThat(result).isInstanceOf(ResourceState.Empty::class.java)
    }

    @Test
    fun `AddFavoriteUseCase should call repository insert`() = runTest {
        // Given
        val character = mockk<Character>()
        val useCase = AddFavoriteUseCase(favoriteRepo)

        // When
        useCase(character)

        // Then
        coVerify { favoriteRepo.addFavorite(character) }
    }
}