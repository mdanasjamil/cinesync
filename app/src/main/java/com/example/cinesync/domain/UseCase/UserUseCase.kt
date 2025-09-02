package com.example.cinesync.domain.UseCase

import com.example.cinesync.data.Database.User
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.UserRepository

open class UserUseCase(
    private var userRepository: UserRepository
) {
    open suspend fun addUser(user: User){
        userRepository.addUser(user)
    }

    open suspend fun verifyLogin(username: String, password: String): User? {
        return userRepository.verifyLogin(username, password)
    }

    open suspend fun insertMovieInUserWatchlist(username:String, movie: Movie):Boolean {
        val alreadyPresent:Boolean = getUserWatchlist(username)?.any { it.title == movie.title && it.posterPath==movie.posterPath}?:false
        if(!alreadyPresent) {
            userRepository.addMovieToWatchlist(username, movie)
            return true
        }
        return false
    }

    open suspend fun getUserWatchlist(username: String): List<Movie>? {
        return userRepository.getWatchlistForUser(username)
    }

    open suspend fun removeMovieFromWatchlist(username:String, movie: Movie) {
        userRepository.removeMovieFromWatchlist(username,movie)

    }

}