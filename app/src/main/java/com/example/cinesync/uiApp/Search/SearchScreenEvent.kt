package com.example.cinesync.uiApp.Search

import com.example.cinesync.domain.Entity.Movie

sealed interface SearchScreenEvent {

    object ScreenLaunched: SearchScreenEvent
    object PerformSearch: SearchScreenEvent

    object LocalWatchlistClicked : SearchScreenEvent
    object GlobalWatchlistClicked : SearchScreenEvent

    data class SearchQueryChanged(val query: String) : SearchScreenEvent
    data class AddToGlobalWatchlist(val movie: Movie) : SearchScreenEvent
    data class AddToLocalWatchlist(val movie: Movie) : SearchScreenEvent

}