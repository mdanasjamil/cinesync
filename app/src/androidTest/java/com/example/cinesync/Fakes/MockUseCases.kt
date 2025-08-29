package com.example.cinesync.fakes

import com.example.cinesync.data.Database.User
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.TmdbRepository
import com.example.cinesync.domain.Repository.UserRepository
import com.example.cinesync.domain.Repository.WatchlistRepository
import com.example.cinesync.domain.UseCase.AddMovieToGlobalWatchlistUseCase
import com.example.cinesync.domain.UseCase.DiscoverMoviesUseCase
import com.example.cinesync.domain.UseCase.GetGlobalWatchlistUseCase
import com.example.cinesync.domain.UseCase.RemoveFromGlobalWatchlistUseCase
import com.example.cinesync.domain.UseCase.SearchMoviesUseCase
import com.example.cinesync.domain.UseCase.UserUseCase
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeDiscoverMoviesUseCase(
    private val repository: TmdbRepository
) : DiscoverMoviesUseCase(repository) {

    private var response: Result<List<Movie>> = Result.success(emptyList())

    fun setResponse(result: Result<List<Movie>>) {
        response = result
    }

    override suspend fun invoke(): Result<List<Movie>> {
        delay(100)
        return response
    }
}

class FakeSearchMoviesUseCase(
    private val repository: TmdbRepository
) : SearchMoviesUseCase(repository) {
}

class FakeUserUseCase(
    private val repository: UserRepository
) : UserUseCase(repository) {
}

class FakeGetGlobalWatchlistUseCase(
    private val repository: WatchlistRepository
) : GetGlobalWatchlistUseCase(repository) {
}

class FakeAddMovieToGlobalWatchlistUseCase(
    private val repository: WatchlistRepository
) : AddMovieToGlobalWatchlistUseCase(repository) {
}