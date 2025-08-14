package com.example.cinesync.uiApp

import android.util.Log
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.UseCase.AddMovieToWatchlistUseCase
import com.example.cinesync.domain.UseCase.DiscoverMoviesUseCase
import com.example.cinesync.domain.UseCase.GetWatchlistUseCase
import com.example.cinesync.domain.UseCase.SearchMoviesUseCase
import com.example.cinesync.uiApp.Search.SearchViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import net.bytebuddy.matcher.ElementMatchers.any
import okhttp3.Dispatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class SearchViewModelTest {
    private lateinit var viewModel: SearchViewModel

    @MockK
    private lateinit var searchMoviesUseCase: SearchMoviesUseCase
    @MockK
    private lateinit var discoverMoviesUseCase: DiscoverMoviesUseCase
    @MockK
    private lateinit var addMovieToWatchlistUseCase: AddMovieToWatchlistUseCase
    @MockK
    private lateinit var getWatchlistUseCase: GetWatchlistUseCase

    @Before
    fun setup(){

        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0

        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = SearchViewModel(searchMoviesUseCase, discoverMoviesUseCase, addMovieToWatchlistUseCase, getWatchlistUseCase)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    //fetchMovies
    @Test
    fun `fetchMovies failure sets error to false and isLoading to true`() = runTest {

        val simulatedError = Exception("Network call failed from test")
        val failureResult: Result<List<Movie>> = Result.failure(simulatedError)
        coEvery {
            discoverMoviesUseCase()
        } returns failureResult

        coEvery{
            getWatchlistUseCase()
        } returns flowOf(Result.success(emptyList()))

        viewModel.fetchMovies()
        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertEquals(
            false,
            finalState.isLoading,
            "isLoading should be false after failure. State: $finalState"
        )
        assertEquals(
            true,
            finalState.error,
            "error should be true after failure. State: $finalState"
        )

    }

    @Test
    fun `fetchMovies success updates state with movie list and sets isLoading to false`() = runTest {
        val mockMovies = listOf(
            Movie(1,"Dragon","xx.jpg",8.2),
            Movie(2,"World","yy.jpg",9.3)
        )

        coEvery{
            discoverMoviesUseCase()
        } returns Result.success(mockMovies)

        coEvery{
            getWatchlistUseCase()
        } returns flowOf(Result.success(emptyList()))

        viewModel.fetchMovies()
        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertEquals(
            false,
            finalState.isLoading,
            "isLoading should be false after success. State: $finalState"
        )
        assertEquals(
            mockMovies,
            finalState.movies,
            "movies should be the same after success. State: $finalState"
        )
        assertEquals(
            false,
            finalState.error,
            "error should be false after success. State: $finalState"
        )
    }

    //fetchSeachedMovie
    @Test
    fun `fetchSearchedMovie failure sets error to true and isLoading to false`() = runTest {
        val simulatedError = Exception("Network call failed from test")
        val failureResult: Result<List<Movie>> = Result.failure(simulatedError)
        coEvery {
            searchMoviesUseCase(any())
        } returns failureResult

        coEvery{
            getWatchlistUseCase()
        } returns flowOf(Result.success(emptyList()))

        viewModel.fetchSearchedMovie("test")
        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertEquals(
            false,
            finalState.isLoading,
            "isLoading should be false after failure. State: $finalState"
        )
        assertEquals(
            true,
            finalState.error,
            "error should be true after failure. State: $finalState"
        )

    }

    @Test
    fun `fetchSearchedMovie success updates state with movie list and sets isLoading to false`() = runTest {
        val mockMovies = listOf(
            Movie(1,"Dragon","xx.jpg",8.2),
            Movie(2,"World","yy.jpg",9.3)
        )
        coEvery{
            searchMoviesUseCase("test")
        } returns Result.success(mockMovies)

        coEvery{
            getWatchlistUseCase()
        } returns flowOf(Result.success(emptyList()))

        viewModel.fetchSearchedMovie("test")
        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertEquals(
            false,
            finalState.isLoading,
            "isLoading should be false after success. State: $finalState"
        )
        assertEquals(
            mockMovies,
            finalState.movies,
            "movies should be the same after success. State: $finalState"
        )
    }

    //addMovieToWatchlist
    @Test
    fun `addMovieToWatchlist failure emits error snackbarMessage`() = runTest {
        val mockMovie = Movie(1,"Dragon","xx.jpg",8.2)
        val simulatedError = Exception("Could Not Add Movie to Watchlist")
        val failureResult: Result<Boolean> = Result.failure(simulatedError)

        coEvery{
            addMovieToWatchlistUseCase(mockMovie)
        } returns failureResult

        coEvery{
            getWatchlistUseCase()
        } returns flowOf(Result.success(emptyList()))


        var collectedMessage: String? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            collectedMessage = viewModel.snackbarMessage.first()
        }

        viewModel.addMovieToWatchlist(mockMovie)
        advanceUntilIdle()

        val finalState = viewModel.uiState.value

        assertEquals("Error: Could not add movie", collectedMessage)
        job.cancel()

    }

    @Test
    fun `addMovieToWatchlist success emits success snackbarMessage`() = runTest {
        val mockMovie = Movie(1,"Dragon","xx.jpg",8.2)
        val mockList = listOf(mockMovie,mockMovie)

        coEvery{
            addMovieToWatchlistUseCase(mockMovie)
        } returns Result.success(true)

        coEvery { getWatchlistUseCase()
        } returns flowOf(Result.success(mockList))

        var collectedMessage: String? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            collectedMessage = viewModel.snackbarMessage.first()
        }

        viewModel.addMovieToWatchlist(mockMovie)
        advanceUntilIdle()
        assertEquals("'${mockMovie.title}' added to Watchlist", collectedMessage)
        job.cancel()
    }

    @Test
    fun `addMovieToWatchlist for existing movie emits correct snackbarMessage`() = runTest {
        val mockMovie = Movie(1,"Dragon","xx.jpg",8.2)
        val mockList = listOf(mockMovie,mockMovie)

        coEvery{
            addMovieToWatchlistUseCase(mockMovie)
        } returns Result.success(false)

        coEvery{
            getWatchlistUseCase()
        } returns flowOf(Result.success(mockList))

        var collectedMessage: String? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)){
            collectedMessage = viewModel.snackbarMessage.first()
        }

        viewModel.addMovieToWatchlist(mockMovie)
        advanceUntilIdle()
        assertEquals("'${mockMovie.title}' is already in the Watchlist", collectedMessage)
        job.cancel()
    }

    @Test
    fun `observeWatchlistChanges success updates state with movie list`() = runTest {
        val mockWatchlist = listOf(
            Movie(1,"Dragon","xx.jpg",8.2),
            Movie(2,"World","yy.jpg",9.3)
        )

        coEvery{
            getWatchlistUseCase()
        } returns flowOf(Result.success(mockWatchlist))

        viewModel.observeWatchlistChanges()
        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertEquals(
            mockWatchlist,
            finalState.watchlistMovies,
            "watchlistMovies should be the same after success. State: $finalState"
        )
    }

    @Test
    fun `observeWatchlistChanges failure sets error to true`() = runTest {
        val simulatedError = Exception("Could Not Fetch Watchlist")
        val failureResult: Result<List<Movie>> = Result.failure(simulatedError)
        coEvery{
            getWatchlistUseCase()
        } returns flowOf(failureResult)

        viewModel.observeWatchlistChanges()
        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertEquals(
            true,
            finalState.error,
            "error should be true after failure. State: $finalState"
        )
        assertEquals(
            emptyList(),
            finalState.watchlistMovies,
            "watchlistMovies should be empty after failure. State: $finalState"
        )
    }
}

/*TODO - Initial state test case*/

