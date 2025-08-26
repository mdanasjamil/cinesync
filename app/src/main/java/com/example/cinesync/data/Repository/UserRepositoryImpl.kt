package com.example.cinesync.data.Repository

import android.util.Log
import com.example.cinesync.data.Database.User
import com.example.cinesync.data.Database.UserDao
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.UserRepository
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
): UserRepository {

    override suspend fun addUser(user: User): Long {
        return userDao.insertUser(user)
    }

    override suspend fun verifyLogin(username: String, password: String): User? {
        Log.e("UserRepositoryImpl", "verifyLogin called with username: $username, password: $password")
        return userDao.verifyLogin(username, password)
    }

    override suspend fun getWatchlistForUser(username: String): List<Movie> {
        Log.d("UserRepositoryImpl", "getWatchlistForUser called with username: $username")
        val user = userDao.getUser(username)
        return user?.movies ?: emptyList()
    }

    override suspend fun addMovieToWatchlist(username: String, newMovie: Movie) {
        val user = userDao.getUser(username)
        if (user != null) {
            val updatedMovies = user.movies?.toMutableList() ?: mutableListOf()
            updatedMovies.add(newMovie)
            userDao.updateUser(user.copy(movies = updatedMovies))
        }
    }

    override suspend fun removeMovieFromWatchlist(username: String, movie: Movie) {
        val user = userDao.getUser(username)
        if (user != null) {
            val updatedMovies = user.movies?.toMutableList() ?: mutableListOf()
            updatedMovies.remove(movie)
            userDao.updateUser(user.copy(movies = updatedMovies))
        }


    }




}