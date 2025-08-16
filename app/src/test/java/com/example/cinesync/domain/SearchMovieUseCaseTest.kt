package com.example.cinesync.domain

import android.util.Log
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.Repository.TmdbRepository
import com.example.cinesync.domain.UseCase.SearchMoviesUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.experimental.theories.suppliers.TestedOn

class SearchMovieUseCaseTest {
    private lateinit var useCase: SearchMoviesUseCase

    @MockK
    private lateinit var repository: TmdbRepository

    @Before
    fun setup(){

        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0

        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        useCase = SearchMoviesUseCase(repository)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should return a list of discovered movies when query is blank` () = runTest{
        val mockMovies = listOf(
            Movie(1, "Dragon", "xx.jpg", 8.2),
            Movie(2,"World","yy.jpg",9.3)
        )

        coEvery{
            repository.discoverMovies()
        } returns mockMovies

        val result = useCase("")
        assert(result.isSuccess)
        assert(result.getOrNull() == mockMovies)

    }

    @Test
    fun `invoke should return a list of searched movies when there is valid query` () = runTest{
        val mockMovies = listOf(
            Movie(1, "Dragon", "xx.jpg", 8.2),
            Movie(2,"Dragon World","yy.jpg",9.3)
        )

        coEvery{
            repository.searchMovies("test")
        } returns mockMovies

        val result = useCase("test")
        assert(result.isSuccess)
        assert(result.getOrNull() == mockMovies)
    }
}