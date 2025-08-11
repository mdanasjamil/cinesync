package com.example.cinesync.uiApp.Search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cinesync.data.MovieDto
import com.example.cinesync.data.TmdbRepositoryImpl
import com.example.cinesync.data.toEntity
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.UseCase.AddMovieToWatchlistUseCase
import com.example.cinesync.domain.UseCase.DiscoverMoviesUseCase
import com.example.cinesync.domain.UseCase.GetWatchlistUseCase
import com.example.cinesync.domain.UseCase.SearchMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val discoverMoviesUseCase: DiscoverMoviesUseCase,
    private val addMovieToWatchlistUseCase: AddMovieToWatchlistUseCase,
    private val getWatchlistUseCase: GetWatchlistUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    init {

        if (_uiState.value.movies.isEmpty()) {
            fetchMovies()
        }
        observeWatchlistChanges()
    }

    fun fetchMovies(){
        viewModelScope.launch {
            _uiState.value.isLoading = true
            discoverMoviesUseCase()
                .onSuccess { movies ->
                    _uiState.update { it.copy(isLoading = false, movies = movies,error = false) }
                }
                .onFailure { error ->
                   logError(error)
                }
        }
    }

    private fun observeWatchlistChanges() {
        viewModelScope.launch {
            getWatchlistUseCase().onSuccess { movies ->
                _uiState.update { it.copy(watchlistMovies = _uiState.value.watchlistMovies + movies) }
            }
        }
    }

    fun fetchSearchedMovie(movieName: String){
        viewModelScope.launch {
            _uiState.value.isLoading = true
            searchMoviesUseCase(movieName)
                .onSuccess { movies ->
                    _uiState.update { it.copy(isLoading = false, movies = movies,error=false) }
                }
                .onFailure { error ->
                   logError(error,movieName)
                }
        }
    }

    fun logError(error: Throwable,query: String=""){
        Log.e("SearchViewModel", "Error fetching movies: $query because ${error.message}", error)
        _uiState.update { it.copy(isLoading = false, error = true, movies = emptyList()) }
    }

    fun addMovieToWatchlist(movie: Movie) {
        viewModelScope.launch {
            addMovieToWatchlistUseCase(movie)
                .onSuccess { wasAdded ->
                    val message = if (wasAdded) {
                        "'${movie.title}' added to Watchlist"
                    } else {
                        "'${movie.title}' is already in the Watchlist"
                    }
                    if(wasAdded){observeWatchlistChanges()} /*TODO repeated fetching and copy, optimize it*/
                    _snackbarMessage.emit(message)
                }
                .onFailure { error ->
                    Log.e("SearchViewModel", "Error adding movie: ${error.message}", error)
                    _snackbarMessage.emit("Error: Could not add movie") // Send error message
                }
        }
    }
}