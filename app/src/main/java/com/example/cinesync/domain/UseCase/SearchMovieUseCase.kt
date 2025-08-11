package com.example.cinesync.domain.UseCase

import android.util.Log
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.TmdbRepository

class SearchMoviesUseCase(
    private val tmdbRepository: TmdbRepository
) {
    suspend operator fun invoke(query: String): Result<List<Movie>> {
        return try {
            var movies: List<Movie> = emptyList()
            if (query.isBlank()) {
                movies = tmdbRepository.discoverMovies()
            } else {
                Log.d("SearchMoviesUseCase", "Query: $query")
                movies = tmdbRepository.searchMovies(query)
            }
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}