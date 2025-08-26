package com.example.cinesync.domain.UseCase

import com.example.cinesync.domain.Entity.Movie

class GetWatchlistFromDatabaseUseCase(
    private val userUseCase: UserUseCase
) {
    suspend operator fun invoke(username: String): List<Movie>? {
        return userUseCase.getUserWatchlist(username)
    }

}