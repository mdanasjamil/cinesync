package com.example.cinesync.uiApp.Search

import com.example.cinesync.domain.Entity.Movie

sealed interface SearchScreenAction {

    data class ShowSnackbar(val message:String): SearchScreenAction

}