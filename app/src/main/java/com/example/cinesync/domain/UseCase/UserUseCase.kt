package com.example.cinesync.domain.UseCase

import com.example.cinesync.data.Database.User
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.UserRepository

class UserUseCase(
    private var userRepository: UserRepository
) {
    suspend fun addUser(user: User){
        userRepository.addUser(user)
    }

    suspend fun verifyLogin(username: String, password: String): User? {
        return userRepository.verifyLogin(username, password)
    }

    suspend fun insertMovieInUserWatchlist(username:String, movie: Movie):Boolean {
        val alreadyPresent:Boolean = getUserWatchlist(username)?.any { it.title == movie.title }?:false
        if(!alreadyPresent) {
            userRepository.addMovieToWatchlist(username, movie)
            return true
        }
        return false
    }

    suspend fun getUserWatchlist(username: String): List<Movie>? {
        return userRepository.getWatchlistForUser(username)
    }

    suspend fun removeMovieFromWatchlist(username:String,movie: Movie) {
        userRepository.removeMovieFromWatchlist(username,movie)

    }

}