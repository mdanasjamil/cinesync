package com.example.cinesync.data.ApiService

import com.example.cinesync.data.MovieListDto
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService{

    @GET("discover/movie")
    suspend fun fetchMovies(
        @Query("api_key") apiKey: String = "6d4f3fd5f6cf3570e56e285129e64b63"
    ): MovieListDto

    @GET("search/movie")
    suspend fun fetchSearchedMovie(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = "6d4f3fd5f6cf3570e56e285129e64b63"
    ): MovieListDto
}
