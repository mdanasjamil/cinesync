package com.example.cinesync.domain.Repository

import com.example.cinesync.domain.Entity.Movie

interface WatchlistRepository {
    suspend fun getWatchlist(): List<Movie>
    suspend fun addToWatchlist(movie: Movie)
    suspend fun removeFromWatchlist(movieId: Int)

}