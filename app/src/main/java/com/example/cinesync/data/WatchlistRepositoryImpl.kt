package com.example.cinesync.data

import com.example.cinesync.data.ApiService.WatchlistApiService
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.WatchlistRepository

class WatchlistRepositoryImpl(
    private val watchlistApiService: WatchlistApiService) : WatchlistRepository {

        override suspend fun getWatchlist(): List<Movie> {
            val dtoList = watchlistApiService.getWatchlist()
            return dtoList.map { it.toEntity() }
        }

        override suspend fun addToWatchlist(movie: Movie) {
            val movieDto = movie.toDto()
            watchlistApiService.addToWatchlist(movieDto)
        }

        override suspend fun removeFromWatchlist(movieId: Int) {
            watchlistApiService.removeFromWatchlist(movieId)
        }
}