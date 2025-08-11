package com.example.cinesync.domain.UseCase

import android.util.Log
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.WatchlistRepository

class GetWatchlistUseCase (
    private val watchlistRepository: WatchlistRepository) {

    suspend operator fun invoke(): Result<List<Movie>> {
        return try {
            val movies = watchlistRepository.getWatchlist()
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}