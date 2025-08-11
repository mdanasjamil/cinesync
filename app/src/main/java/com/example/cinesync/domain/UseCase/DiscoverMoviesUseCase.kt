package com.example.cinesync.domain.UseCase

import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.TmdbRepository

class DiscoverMoviesUseCase(
    private val repository: TmdbRepository
) {
    suspend operator fun invoke(): Result<List<Movie>> {
        return try {
            val movies = repository.discoverMovies()
            val filteredList = movies
                .filter { it.posterPath.isNotEmpty() && it.voteAverage >= 7.0 }
            Result.success(filteredList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}