package com.example.cinesync.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto (
    val id: Int,
    val title: String?,
    val vote_average: Double?,
    @SerialName("poster_path")
    val poster_path: String?
)

@Serializable
data class MovieListDto(
    val results: List<MovieDto>
)