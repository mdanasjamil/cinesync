package com.example.cinesync.data.ApiService

import com.example.cinesync.data.MovieDto
import com.example.cinesync.data.MovieListDto
import com.example.cinesync.domain.Entity.Movie
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WatchlistApiService{
    @GET("watchlist/movies")
    suspend fun getWatchlist(): List<MovieDto>

    @POST("watchlist/movies")
    suspend fun addToWatchlist(@Body movie: MovieDto)

    @DELETE("watchlist/movies/{movieId}")
    suspend fun removeFromWatchlist(@Path("movieId") movieId: Int)

    @GET("search/movie")
    suspend fun searchForExistingMovie(@Query("query") query: String): MovieListDto


}