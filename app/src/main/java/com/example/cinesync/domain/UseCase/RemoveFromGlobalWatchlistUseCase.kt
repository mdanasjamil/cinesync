package com.example.cinesync.domain.UseCase

import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.WatchlistRepository

open class RemoveFromGlobalWatchlistUseCase(
    private val watchlistRepository: WatchlistRepository
) {
    open suspend operator fun invoke(movie: Movie): Result<Unit>{
        return try{
            watchlistRepository.removeFromWatchlist(movie.id)
            Result.success(Unit)
        } catch(e: Exception){
            Result.failure(e)
        }
    }
}