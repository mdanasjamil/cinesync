package com.example.cinesync.uiApp.Watchlist

import com.example.cinesync.domain.Entity.Movie

data class WatchlistUiState (
    var movies: List<Movie> = emptyList(),
    var isLoading: Boolean = true,
    var error: Boolean = false,
){}