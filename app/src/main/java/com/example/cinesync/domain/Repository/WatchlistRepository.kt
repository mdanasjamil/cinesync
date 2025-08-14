package com.example.cinesync.domain.Repository

import com.example.cinesync.domain.Entity.Movie
import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    suspend fun getWatchlist(): Flow<List<Movie>>
    suspend fun addToWatchlist(movie: Movie)
    suspend fun removeFromWatchlist(movieId: Int)

}