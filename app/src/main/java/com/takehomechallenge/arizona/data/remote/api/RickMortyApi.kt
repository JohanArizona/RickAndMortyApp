package com.takehomechallenge.arizona.data.remote.api

import com.takehomechallenge.arizona.data.remote.dto.model.CharacterDto
import com.takehomechallenge.arizona.data.remote.dto.response.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickMortyApi {

    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): ApiResponse

    @GET("character/{id}")
    suspend fun getCharacterDetail(@Path("id") id: Int): CharacterDto

    @GET("character")
    suspend fun searchCharacters(
        @Query("name") name: String,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: String? = null,
        @Query("page") page: Int = 1
    ): ApiResponse
}