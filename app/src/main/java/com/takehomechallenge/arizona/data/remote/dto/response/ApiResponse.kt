package com.takehomechallenge.arizona.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import com.takehomechallenge.arizona.data.remote.dto.model.CharacterDto

data class ApiResponse(
    @SerializedName("info") val info: InfoDto,
    @SerializedName("results") val results: List<CharacterDto>
)

data class InfoDto(
    @SerializedName("count") val count: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("prev") val prev: String?
)