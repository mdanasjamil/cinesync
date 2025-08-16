package com.example.cinesync.domain.UseCase

import android.util.Log
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.WatchlistRepository

class RemoveFromWatchlistUseCase(
    private val watchlistRepository: WatchlistRepository
) {
    suspend operator fun invoke(movie: Movie): Result<Unit>{
        return try{
            watchlistRepository.removeFromWatchlist(movie.id)
            Result.success(Unit)
        } catch(e: Exception){
            Result.failure(e)
        }
    }
}