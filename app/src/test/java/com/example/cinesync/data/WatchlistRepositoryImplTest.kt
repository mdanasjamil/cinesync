package com.example.cinesync.data

import com.example.cinesync.data.ApiService.WatchlistApiService
import com.example.cinesync.data.Repository.WatchlistRepositoryImpl
import com.example.cinesync.domain.Entity.Movie
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class WatchlistRepositoryImplTest {


    @MockK
    private lateinit var watchlistApiService: WatchlistApiService

    private lateinit var watchlistRepository: WatchlistRepositoryImpl

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        watchlistRepository = WatchlistRepositoryImpl(watchlistApiService)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `getWatchlist on success updates flow with movies`() = runTest {
        val mockDtoList = listOf(MovieDto(1, "Inception", 8.8, "/poster.jpg"))
        val expectedMovies = mockDtoList.map { it.toEntity() }
        coEvery { watchlistApiService.getWatchlist() } returns mockDtoList

        val result = watchlistRepository.getWatchlist().first()

        assertEquals(expectedMovies, result)
    }

    @Test
    fun `getWatchlist on failure updates flow with empty list`() = runTest {
        coEvery { watchlistApiService.getWatchlist() } throws Exception("Network Failed")

        val result = watchlistRepository.getWatchlist().first()

        assertEquals(emptyList<Movie>(), result)
    }

    @Test
    fun `addToWatchlist calls api and refreshes flow`() = runTest {
        val movieToAdd = Movie(1, "New Movie", "/new.jpg", 9.0)
        val expectedDto = movieToAdd.toDto()

        coEvery { watchlistApiService.addToWatchlist(any()) } returns Unit
        coEvery { watchlistApiService.getWatchlist() } returns listOf(expectedDto)

        watchlistRepository.addToWatchlist(movieToAdd)
        val result = watchlistRepository.getWatchlist().first()

        coVerify(exactly = 1) { watchlistApiService.addToWatchlist(expectedDto) }
        assertEquals(1, result.size)
        assertEquals("New Movie", result[0].title)
    }

    @Test
    fun `removeFromWatchlist calls api and refreshes flow`() = runTest {
        val movieIdToRemove = 123

        coEvery { watchlistApiService.removeFromWatchlist(movieIdToRemove) } returns Unit
        coEvery { watchlistApiService.getWatchlist() } returns emptyList()

        watchlistRepository.removeFromWatchlist(movieIdToRemove)
        val result = watchlistRepository.getWatchlist().first()

        coVerify(exactly = 1) { watchlistApiService.removeFromWatchlist(movieIdToRemove) }
        assertEquals(true, result.isEmpty())
    }
}