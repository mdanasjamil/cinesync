package com.example.cinesync.uiApp

import com.example.cinesync.data.ApiService.MoviesApiService
import com.example.cinesync.data.ApiService.WatchlistApiService
import com.example.cinesync.data.TmdbRepositoryImpl
import com.example.cinesync.data.WatchlistRepositoryImpl
import com.example.cinesync.domain.Repository.TmdbRepository
import com.example.cinesync.domain.Repository.WatchlistRepository
import com.example.cinesync.domain.UseCase.AddMovieToWatchlistUseCase
import com.example.cinesync.domain.UseCase.DiscoverMoviesUseCase
import com.example.cinesync.domain.UseCase.GetWatchlistUseCase
import com.example.cinesync.domain.UseCase.RemoveFromWatchlistUseCase
import com.example.cinesync.domain.UseCase.SearchMoviesUseCase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoviesApiService(): MoviesApiService {
        val okHttpClient = OkHttpClient.Builder().build()
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(MoviesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTmdbRepository(apiService: MoviesApiService): TmdbRepository {
        return TmdbRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideDiscoverMoviesUseCase(repository: TmdbRepository): DiscoverMoviesUseCase {
        return DiscoverMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchMoviesUseCase(repository: TmdbRepository): SearchMoviesUseCase {
        return SearchMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideWatchlistApiService(): WatchlistApiService {
        val okHttpClient = OkHttpClient.Builder().build()
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://6890e3e2944bf437b597aad2.mockapi.io/cinesync/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(WatchlistApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideWatchlistRepository(apiService: WatchlistApiService): WatchlistRepository {
        return WatchlistRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetWatchlistUseCase(repository: WatchlistRepository): GetWatchlistUseCase {
        return GetWatchlistUseCase(repository)

    }

    @Provides
    @Singleton
    fun provideAddMovieToWatchlistUseCase(repository: WatchlistRepository): AddMovieToWatchlistUseCase {
        return AddMovieToWatchlistUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRemoveFromWatchlistUseCase(repository: WatchlistRepository): RemoveFromWatchlistUseCase {
        return RemoveFromWatchlistUseCase(repository)
    }
}