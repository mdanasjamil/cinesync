package com.example.cinesync.domain.Repository

import com.example.cinesync.data.Database.User
import com.example.cinesync.domain.Entity.Movie

interface UserRepository {

    suspend fun addUser(user: User): Long
    suspend fun verifyLogin(username: String, password: String): User?
    suspend fun addMovieToWatchlist(username: String, movie: Movie): Unit
    suspend fun getWatchlistForUser(username: String): List<Movie>
    suspend fun removeMovieFromWatchlist(username:String,movie: Movie): Unit

}