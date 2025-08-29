package com.example.cinesync.uiApp.Watchlist

import android.util.Log
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.UseCase.GetGlobalWatchlistUseCase
import com.example.cinesync.domain.UseCase.RemoveFromGlobalWatchlistUseCase
import com.example.cinesync.uiApp.GlobalWatchlist.WatchlistViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class WatchlistViewModelTest {
    private lateinit var viewModel: WatchlistViewModel

    @MockK
    private lateinit var getGlobalWatchlistUseCase: GetGlobalWatchlistUseCase
    @MockK
    private lateinit var removeFromGlobalWatchlistUseCase: RemoveFromGlobalWatchlistUseCase

    val mockWatchlist = listOf(
        Movie(1, "Dragon", "xx.jpg", 8.2),
        Movie(2, "World", "yy.jpg", 9.3)
    )

    @Before
    fun setup(){

        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())

        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0

        coEvery {
            getGlobalWatchlistUseCase()
        } returns flowOf(
            Result.success(
                listOf(
                    Movie(1, "Dragon", "xx.jpg", 8.2),
                    Movie(2, "World", "yy.jpg", 9.3)
                )
            )
        )
        viewModel = WatchlistViewModel(getGlobalWatchlistUseCase, removeFromGlobalWatchlistUseCase)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `removeMovieFromGlobalWatchlist on success emits correct snackbar message` () = runTest {
        val movie = mockWatchlist[0]

        coEvery {
            removeFromGlobalWatchlistUseCase(movie)
        } returns Result.success(Unit)

        var collectedMessage: String? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            collectedMessage = viewModel.snackbarMessage.first()
        }

        viewModel.removeMovieFromGlobalWatchlist(movie)
        advanceUntilIdle()

        assertEquals("'${movie.title}' removed from Global Watchlist", collectedMessage)
        job.cancel()
    }
}