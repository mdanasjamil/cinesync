package com.example.cinesync.domain.UseCase

import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.WatchlistRepository
import kotlinx.coroutines.flow.firstOrNull

open class AddMovieToGlobalWatchlistUseCase(
    private val watchlistRepository: WatchlistRepository,
    ) {

    open suspend operator fun invoke(movie: Movie): Result<Boolean> {
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