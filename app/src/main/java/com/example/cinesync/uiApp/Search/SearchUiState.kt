package com.example.cinesync.uiApp.Search

import com.example.cinesync.domain.Entity.Movie

data class SearchUiState(
    var movies: List<Movie> = emptyList(),
    var isLoading: Boolean = true,
    var error: Boolean = false,
    val watchlistMovies: List<Movie> = emptyList()
) {

}