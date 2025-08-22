package com.example.cinesync.domain

import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.TmdbRepository
import com.example.cinesync.domain.UseCase.DiscoverMoviesUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
class DiscoverMoviesUseCaseTest {
    @MockK
    private lateinit var tmdbRepository: TmdbRepository

    private lateinit var useCase: DiscoverMoviesUseCase

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        useCase = DiscoverMoviesUseCase(tmdbRepository)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke return a list of filtered movies on success`() = runTest{
        val movies = listOf(
            Movie(1, "Dragon", "xx.jpg", 6.1),
            Movie(2,"World","yy.jpg",9.3),
            Movie(3,"Star","",7.9)
        )

        coEvery{
            tmdbRepository.discoverMovies()
        } returns movies

        val resultMovies = useCase().getOrThrow()

        assertEquals(1, resultMovies.size)
        assertEquals("World", resultMovies[0].title)
    }

    @Test
    fun `invoke returns a failure when the repository throws an exception`() = runTest{
        val simulatedError = Exception("Network error")
        coEvery{
            tmdbRepository.discoverMovies()
        } throws simulatedError

        val result = useCase()
        assertEquals(true, result.isFailure)
        assertEquals(simulatedError, result.exceptionOrNull())
    }

    @Test
    fun `invoke returns a empty list when no movies meet the filtering criteria`() = runTest{
        val movies = listOf(
            Movie(1, "Dragon", "xx.jpg", 6.1),
            Movie(2,"World","yy.jpg",4.3),
            Movie(3,"Star","zz.jpg",6.9)
        )
        coEvery{
            tmdbRepository.discoverMovies()
        } returns movies

        val resultMovies = useCase().getOrThrow()
        assertTrue(resultMovies.isEmpty())

    }

    @Test
    fun `invoke returns an empty list when repository returns an empty list`() = runTest {
        coEvery {
            tmdbRepository.discoverMovies()
        } returns emptyList()

        val result = useCase().getOrThrow()

        assertTrue(result.isEmpty())
    }
}