package com.example.cinesync.uiApp.Search

import com.example.cinesync.domain.Entity.Movie

data class SearchUiState(
    var movies: List<Movie> = emptyList(),
    var isLoading: Boolean = true,
    var error: Boolean = false,
    var searchQuery: String? = "",
    val localWatchlistMovies: List<Movie> = emptyList(),
    val globalWatchlistMovies: List<Movie> = emptyList()
) {

}