package com.example.cinesync.domain.UseCase

import android.util.Log
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.WatchlistRepository
import kotlinx.coroutines.flow.firstOrNull

class AddMovieToWatchlistUseCase(
    private val watchlistRepository: WatchlistRepository,
    ) {

    suspend operator fun invoke(movie: Movie): Result<Boolean> {
        return try {
            val currentWatchlist: List<Movie> = watchlistRepository.getWatchlist().firstOrNull() ?: emptyList()
                val isAlreadyInWatchlist = currentWatchlist
                .any { it.title == movie.title && it.posterPath==movie.posterPath }
            if(!isAlreadyInWatchlist) {
                watchlistRepository.addToWatchlist(movie)
                Result.success(true)
            } else{
                Result.success(false)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}