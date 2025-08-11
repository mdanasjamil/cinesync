package com.example.cinesync.data

import android.util.Log
import com.example.cinesync.data.ApiService.MoviesApiService
import com.example.cinesync.domain.Repository.TmdbRepository
import com.example.cinesync.domain.Entity.Movie

class TmdbRepositoryImpl(
    private val apiService: MoviesApiService
) : TmdbRepository {

    override suspend fun searchMovies(query: String): List<Movie> {
        val dtoList = apiService.fetchSearchedMovie(query).results
        return dtoList.map { it.toEntity() }
    }

    override suspend fun discoverMovies(): List<Movie> {
        val dtoList = apiService.fetchMovies().results
        return dtoList.map { it.toEntity() }
    }
}