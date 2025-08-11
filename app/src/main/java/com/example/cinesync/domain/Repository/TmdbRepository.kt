package com.example.cinesync.domain.Repository

import com.example.cinesync.domain.Entity.Movie

interface TmdbRepository {
    suspend fun searchMovies(query: String): List<Movie>
    suspend fun discoverMovies(): List<Movie>
}