package com.example.cinesync.data

import com.example.cinesync.data.ApiService.MoviesApiService
import com.example.cinesync.data.Repository.TmdbRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.DefaultAsserter.assertTrue

class TmdbRepositoryImplTest {

    @MockK
    private lateinit var moviesApiService: MoviesApiService

    private lateinit var tmdbRepository: TmdbRepositoryImpl

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        tmdbRepository = TmdbRepositoryImpl(moviesApiService)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `discoverMovies success returns a list of movies`() = runTest {
        val mockDtoList = MovieListDto(
            results = listOf(MovieDto(1, "Movie 1", 8.0, "/poster1.jpg"))
        )
        coEvery { moviesApiService.fetchMovies() } returns mockDtoList

        val result = tmdbRepository.discoverMovies()

        assertEquals(1, result.size)
        assertEquals("Movie 1", result[0].title)
    }

    @Test
    fun `discoverMovies failure throws exception`() = runTest {
        val simulatedError = Exception("Network failed")
        coEvery { moviesApiService.fetchMovies() } throws simulatedError

        try {
            tmdbRepository.discoverMovies()
            assertTrue("Expected an exception to be thrown", false)
        } catch (e: Exception) {
            assertEquals(simulatedError, e)
        }
    }

    @Test
    fun `searchMovies success returns a list of movies`() = runTest {
        val query = "Inception"
        val mockDtoList = MovieListDto(
            results = listOf(MovieDto(2, "Inception", 8.8, "/poster2.jpg"))
        )
        coEvery { moviesApiService.fetchSearchedMovie(query) } returns mockDtoList

        val result = tmdbRepository.searchMovies(query)

        assertEquals(1, result.size)
        assertEquals("Inception", result[0].title)
    }
}
