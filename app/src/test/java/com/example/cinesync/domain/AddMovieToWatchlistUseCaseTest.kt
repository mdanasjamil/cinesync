package com.example.cinesync.domain

import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.WatchlistRepository
import com.example.cinesync.domain.UseCase.AddMovieToGlobalWatchlistUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.DefaultAsserter.assertEquals
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class AddMovieToGlobalWatchlistUseCaseTest {

    @MockK
    private lateinit var watchlistRepository: WatchlistRepository
    private lateinit var useCase: AddMovieToGlobalWatchlistUseCase

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        useCase = AddMovieToGlobalWatchlistUseCase(watchlistRepository)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `movie is added to global watchlist when it is not already present` () = runTest{
        val watchlist = listOf(Movie(1, "Dragon", "xx.jpg", 8.2), Movie(2, "World", "yy.jpg", 9.3))
        coEvery{
            watchlistRepository.getWatchlist()
        } returns flowOf(watchlist)

        coEvery {
            watchlistRepository.addToWatchlist(any())
        } returns Unit
        val newMovie = Movie(3, "Star", "zz.jpg", 7.9)
        val result = useCase(newMovie)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { watchlistRepository.addToWatchlist(any()) }

    }

    @Test
    fun `movie is not added to global watchlist when it is already present` () = runTest{
        val watchlist = listOf(Movie(1, "Dragon", "xx.jpg", 8.2),
                                Movie(2, "World", "yy.jpg", 9.3))
        coEvery {
            watchlistRepository.getWatchlist()
        } returns flowOf(watchlist)

        val newMovie = Movie(1, "Dragon", "xx.jpg", 8.2)
        val result = useCase(newMovie)

        assertTrue(result.isSuccess)
        assertEquals("Result should contain 'false' when movie is already present", false, result.getOrNull())
        coVerify(exactly = 0) { watchlistRepository.addToWatchlist(any()) }
    }

    @Test
    fun `invoke WHEN repository throws an exception THEN returns failure`() = runTest {
        val movieToAdd = Movie(id = 1, title = "New Movie", posterPath = "/new.jpg", voteAverage = 8.0)
        val exception = IOException("Network error")

        coEvery { watchlistRepository.getWatchlist() } throws exception

        val result = useCase(movieToAdd)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }



}