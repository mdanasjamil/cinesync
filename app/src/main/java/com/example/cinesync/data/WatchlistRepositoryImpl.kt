package com.example.cinesync.data

import com.example.cinesync.data.ApiService.WatchlistApiService
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class WatchlistRepositoryImpl(
    private val watchlistApiService: WatchlistApiService) : WatchlistRepository {

        private val _watchlistFlow = MutableStateFlow<List<Movie>> (emptyList())

        override suspend fun getWatchlist(): Flow<List<Movie>> {
            refreshWatchlist()
            return _watchlistFlow
        }

        suspend fun refreshWatchlist() {
            try {
                val dtoList = watchlistApiService.getWatchlist()
                _watchlistFlow.value = dtoList.map { it.toEntity() }
            }catch(e: Exception){
                _watchlistFlow.value = emptyList()
            }
        }

        override suspend fun addToWatchlist(movie: Movie) {
            val movieDto = movie.toDto()
            watchlistApiService.addToWatchlist(movieDto)
            refreshWatchlist()
        }

        override suspend fun removeFromWatchlist(movieId: Int) {
            watchlistApiService.removeFromWatchlist(movieId)
            refreshWatchlist()
        }
}