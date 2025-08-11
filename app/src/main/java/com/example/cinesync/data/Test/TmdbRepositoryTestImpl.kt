package com.example.cinesync.data.Test

import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.TmdbRepository

class TmdbRepositoryTestImpl : TmdbRepository {

    private val dummyMovies = listOf(
        Movie(
            id = 1,
            title = "Good Movie",
            posterPath = "drawable/inception.jpg",
            voteAverage = 8.0
        ),
        Movie(id = 2, title = "Movie Without Poster", posterPath = "", voteAverage = 7.0)
    )

    override suspend fun searchMovies(query: String): List<Movie>{
        return dummyMovies
    }
    override suspend fun discoverMovies(): List<Movie>{
        return dummyMovies
    }
}