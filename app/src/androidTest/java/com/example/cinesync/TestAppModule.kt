package com.example.cinesync


import com.example.cinesync.domain.Repository.TmdbRepository
import com.example.cinesync.domain.Repository.UserRepository
import com.example.cinesync.domain.Repository.WatchlistRepository
import com.example.cinesync.domain.UseCase.*
import com.example.cinesync.fakes.FakeAddMovieToGlobalWatchlistUseCase
import com.example.cinesync.fakes.FakeDiscoverMoviesUseCase
import com.example.cinesync.fakes.FakeGetGlobalWatchlistUseCase
//import com.example.cinesync.fakes.FakeRemoveFromGlobalWatchlistUseCase
import com.example.cinesync.fakes.FakeSearchMoviesUseCase
import com.example.cinesync.fakes.FakeUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    // Provide fakes for ALL UseCases used by SearchViewModel
    @Provides
    @Singleton
    fun provideTmdbRepository(): TmdbRepository = mockk(relaxed = true)

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository = mockk(relaxed = true)

    @Provides
    @Singleton
    fun provideWatchlistRepository(): WatchlistRepository = mockk(relaxed = true)


    // --- 2. Provide Fake UseCases Using the Mocks Above ---
    @Provides
    @Singleton
    fun provideDiscoverMoviesUseCase(repo: TmdbRepository): DiscoverMoviesUseCase =
        FakeDiscoverMoviesUseCase(repo)

    @Provides
    @Singleton
    fun provideSearchMoviesUseCase(repo: TmdbRepository): SearchMoviesUseCase =
        FakeSearchMoviesUseCase(repo)

    @Provides
    @Singleton
    fun provideUserUseCase(repo: UserRepository): UserUseCase =
        FakeUserUseCase(repo)

    @Provides
    @Singleton
    fun provideGetGlobalWatchlistUseCase(repo: WatchlistRepository): GetGlobalWatchlistUseCase =
        FakeGetGlobalWatchlistUseCase(repo)

    @Provides
    @Singleton
    fun provideAddMovieToGlobalWatchlistUseCase(repo: WatchlistRepository): AddMovieToGlobalWatchlistUseCase =
        FakeAddMovieToGlobalWatchlistUseCase(repo)

    @Provides
    @Singleton // Or another scope if needed
    fun provideRemoveFromGlobalWatchlistUseCase(watchlistRepository: WatchlistRepository): RemoveFromGlobalWatchlistUseCase {
        return RemoveFromGlobalWatchlistUseCase(watchlistRepository)
    }
}