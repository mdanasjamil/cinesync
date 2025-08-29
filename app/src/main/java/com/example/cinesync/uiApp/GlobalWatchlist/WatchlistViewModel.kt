package com.example.cinesync.uiApp.GlobalWatchlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.UseCase.GetGlobalWatchlistUseCase
import com.example.cinesync.domain.UseCase.RemoveFromGlobalWatchlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val getGlobalWatchlistUseCase: GetGlobalWatchlistUseCase,
    private val removeFromGlobalWatchlistUseCase: RemoveFromGlobalWatchlistUseCase
):ViewModel() {
    private val _uiState = MutableStateFlow(WatchlistUiState())
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    fun getGlobalWatchlist() {
        viewModelScope.launch {
            _uiState.value.isLoading = true
            getGlobalWatchlistUseCase().collect { Result ->
                Result.onSuccess { movies ->
                    _uiState.update { it.copy(isLoading = false, movies = movies, error = false) }
                    Log.d(
                        "GetWatchlistUseCase",
                        "Watchlist fetched successfully with size = ${movies.size}"
                    )

                }.onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = true, movies = emptyList<Movie>())
                    }
                    Log.e("GetWatchlistUseCase", "Error fetching watchlist", error)
                }
            }
        }
    }

    fun removeMovieFromGlobalWatchlist(movie: Movie) {
        viewModelScope.launch {
            _uiState.value.isLoading = true
            removeFromGlobalWatchlistUseCase(movie)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, error = false) }
                    getGlobalWatchlist()
                    val message = "'${movie.title}' removed from Global Watchlist"
                    Log.d("RemoveFromWatchlistUseCase", "Movie removed from Global Watchlist: ${movie.title}")
                    _snackbarMessage.emit(message)
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = true) }
                    val message = "Error: Could not remove movie"
                    _snackbarMessage.emit(message)
                    Log.e("RemoveFromWatchlistUseCase", "Error removing movie from Global Watchlist: ${movie.title} because ${error.message}")

                }

        }
    }
}