package com.example.cinesync.uiApp.LocalWatchlist

import com.example.cinesync.domain.Entity.Movie

data class LocalWatchlistUiState (
    var movies: List<Movie> = emptyList(),
    var isLoading: Boolean = true,
    var error: Boolean = false,
){}