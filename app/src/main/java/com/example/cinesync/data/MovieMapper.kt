package com.example.cinesync.data

import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.data.MovieDto

fun MovieDto.toEntity(): Movie {
    return Movie(
        id = this.id,
        title = this.title?:"",
        posterPath = this.poster_path?:"",
        voteAverage = this.vote_average?:0.0,
    )
}

fun Movie.toDto(): MovieDto {
    return MovieDto(
        id = this.id,
        title = this.title,
        poster_path = this.posterPath,
        vote_average = this.voteAverage
    )
}