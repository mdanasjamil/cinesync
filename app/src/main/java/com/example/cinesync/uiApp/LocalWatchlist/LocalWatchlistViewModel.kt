package com.example.cinesync.uiApp.LocalWatchlist


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinesync.data.Database.User
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.UseCase.GetWatchlistFromDatabaseUseCase
import com.example.cinesync.domain.UseCase.GetWatchlistUseCase
import com.example.cinesync.domain.UseCase.RemoveFromWatchlistUseCase
import com.example.cinesync.domain.UseCase.UserUseCase
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
class LocalWatchlistViewModel @Inject constructor(
    private val getWatchlistUseCase: GetWatchlistUseCase,
    private val getWatchlistFromDatabaseUseCase: GetWatchlistFromDatabaseUseCase,
    private val removeFromWatchlistUseCase: RemoveFromWatchlistUseCase,
    private val userUseCase: UserUseCase
):ViewModel() {
    private val _uiState = MutableStateFlow(LocalWatchlistUiState())
    val uiState: StateFlow<LocalWatchlistUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

//    init {
//        if (_uiState.value.movies.isEmpty()) {
//            getWatchlist()
//        }
//    }

    fun getLocalWatchlist(user: User?) {
        if (user == null || user.username.isNullOrBlank()) {
            _uiState.update { it.copy(isLoading = false, error = true) }
            Log.e("GetWatchlistUseCase", "Cannot fetch watchlist because user or username is null.")
            return
        }
        viewModelScope.launch {
            _uiState.value.isLoading = true
            try{
                val movies = userUseCase.getUserWatchlist(user.username)
                _uiState.update { it.copy(isLoading = false, movies=movies?:emptyList(), error = false) }
                Log.d("GetWatchlistUseCase", "Local Watchlist fetched successfully with size: ${user.movies?.size}")
            }catch (e: Exception){
                _uiState.update { it.copy(isLoading = false, error = true) }
                Log.e("GetWatchlistUseCase", "Error fetching watchlist", e)
            }

        }
    }

    fun removeMovieFromLocalWatchlist(user: User?, movie: Movie) {
        if (user == null || user.username.isNullOrBlank()) {
            _uiState.update { it.copy(isLoading = false, error = true) }
            Log.e("GetWatchlistUseCase", "Cannot fetch watchlist because user or username is null.")
            return
        }
        viewModelScope.launch {
            _uiState.value.isLoading = true
            try{
                userUseCase.removeMovieFromWatchlist(user.username,movie)
                val movies = userUseCase.getUserWatchlist(user.username)
                _uiState.update{it.copy(isLoading = false, error = false,movies = movies?:emptyList())}
            }catch(e: Exception){
                _uiState.update { it.copy(isLoading = false, error = true) }
                Log.e("GetWatchlistUseCase", "Error fetching watchlist", e)
            }

        }
    }
}