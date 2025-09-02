package com.example.cinesync.uiApp.Search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesync.data.Database.User
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.UseCase.AddMovieToGlobalWatchlistUseCase
import com.example.cinesync.domain.UseCase.DiscoverMoviesUseCase
import com.example.cinesync.domain.UseCase.GetGlobalWatchlistUseCase
import com.example.cinesync.domain.UseCase.SearchMoviesUseCase
import com.example.cinesync.domain.UseCase.UserUseCase
import com.example.cinesync.uiApp.Login.LoginAuthScreenAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val discoverMoviesUseCase: DiscoverMoviesUseCase,
    private val addMovieToGlobalWatchlistUseCase: AddMovieToGlobalWatchlistUseCase,
    private val getGlobalWatchlistUseCase: GetGlobalWatchlistUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _action = Channel<SearchScreenAction>(capacity=Channel.BUFFERED)
    val action = _action.receiveAsFlow()

    private fun sendAction(action: SearchScreenAction){
        _action.trySend(action)
    }

    fun fetchMovies(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = false) }
            discoverMoviesUseCase()
                .onSuccess { movies ->
                    _uiState.update { it.copy(isLoading = false, movies = movies,error = false) }
                }
                .onFailure { error ->
                   logError(error)
                }
        }
    }

    fun observeGlobalWatchlistChanges() {
        viewModelScope.launch {
            getGlobalWatchlistUseCase().collect { Result ->
                Result.onSuccess { watchlistMovies ->
                    _uiState.update { it.copy(globalWatchlistMovies = watchlistMovies)}
                }.onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = true, movies = emptyList<Movie>())
                    }
                    Log.e("GetWatchlistUseCase", "Error fetching watchlist", error)
                }
            }
        }
    }
    fun observeLocalWatchlistChanges(user:User?){
            if (user == null || user.username.isNullOrBlank()) {
                _uiState.update { it.copy(isLoading = false, error = true) }
                Log.e("GetWatchlistUseCase", "Cannot fetch watchlist because user or username is null.")
                return
            }
            viewModelScope.launch {
                _uiState.value.isLoading = true
                try{
                    val movies = userUseCase.getUserWatchlist(user.username)
                    _uiState.update { it.copy(localWatchlistMovies = movies?:emptyList()) }
                    Log.d("GetWatchlistUseCase", "Local Watchlist fetched successfully with size: ${user.movies?.size}")
                }catch (e: Exception){
                    _uiState.update { it.copy(isLoading = false, error = true) }
                    Log.e("GetWatchlistUseCase", "Error fetching local watchlist", e)
                }

            }
    }

    fun fetchSearchedMovie(movieName: String){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = false) }
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
        Log.e("SearchViewMode", "Is loading: ${_uiState.value.isLoading}")
    }

    fun addMovieToGlobalWatchlist(movie: Movie) {
        viewModelScope.launch {
            addMovieToGlobalWatchlistUseCase(movie)
                .onSuccess { wasAdded ->
                    val message = if (wasAdded) {
                        "'${movie.title}' added to Global Watchlist"
                    } else {
                        "'${movie.title}' is already in the Global Watchlist"
                    }
                    if(wasAdded){observeGlobalWatchlistChanges()} /*TODO repeated fetching and copy, optimize it*/
                    sendAction(SearchScreenAction.ShowSnackbar(message))
                }
                .onFailure { error ->
                    Log.e("SearchViewModel", "Error adding movie: ${error.message}", error)
                    val message = "Error: Could not add movie"
                    sendAction(SearchScreenAction.ShowSnackbar(message))
                }
        }
    }
    fun addMovieToLocalWatchlist(user: User?, movie: Movie) {
        viewModelScope.launch {
            if (user?.username == null) {
                val message= "Error: User not logged in."
                sendAction(SearchScreenAction.ShowSnackbar(message))
                return@launch
            }
            try {
                val movieWasAdded = userUseCase.insertMovieInUserWatchlist(user.username, movie)
                val message = if (movieWasAdded) {
                    "'${movie.title}' added to Local Watchlist"
                } else {
                    "'${movie.title}' is already in the Local Watchlist"
                }
                sendAction(SearchScreenAction.ShowSnackbar(message))

                if (movieWasAdded) {
                    _uiState.update { it.copy(localWatchlistMovies = it.localWatchlistMovies + movie) }
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error adding movie locally: ${e.message}", e)
                val message = "Error: Could not add movie"
                sendAction(SearchScreenAction.ShowSnackbar(message))
            }
        }
    }

    fun onEvent(user:User?, event: SearchScreenEvent){
        when(event){
            is SearchScreenEvent.ScreenLaunched -> {
                fetchMovies()
                observeGlobalWatchlistChanges()
                observeLocalWatchlistChanges(user)
            }
            is SearchScreenEvent.SearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }
            is SearchScreenEvent.PerformSearch -> {
                fetchSearchedMovie(_uiState.value.searchQuery?:"")
            }
            is SearchScreenEvent.AddToGlobalWatchlist -> {
                addMovieToGlobalWatchlist(movie = event.movie)
            }
            is SearchScreenEvent.AddToLocalWatchlist -> {
                addMovieToLocalWatchlist(user,event.movie)
            }
            else -> Unit
        }

    }
}