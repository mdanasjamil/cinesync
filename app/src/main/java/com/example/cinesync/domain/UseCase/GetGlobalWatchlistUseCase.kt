package com.example.cinesync.domain.UseCase

import android.util.Log
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

open class GetGlobalWatchlistUseCase (
    private val watchlistRepository: WatchlistRepository) {

    open suspend operator fun invoke(): Flow<Result<List<Movie>>> {
        return watchlistRepository.getWatchlist()
         .map { movies -> Result.success(movies) }
         .catch { e ->
             Log.e("GetWatchlistUseCase", "Error in watchlist repository", e)
             emit(Result.failure(e))
         }
    }
}