package com.example.cinesync.UiApp.Search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.cinesync.MainActivity
import com.example.cinesync.TestAppModule
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.domain.UseCase.DiscoverMoviesUseCase
import com.example.cinesync.fakes.FakeDiscoverMoviesUseCase

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import com.example.cinesync.uiApp.AppModule
import com.example.cinesync.uiApp.Search.SearchScreen
import com.example.cinesync.uiApp.Search.SearchViewModel

@HiltAndroidTest
@UninstallModules(AppModule::class)
class SearchScreenTest {


    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var discoverMoviesUseCase: DiscoverMoviesUseCase

    @Before
    fun setUp() {
        hiltRule.inject()
//        composeTestRule.setContent {
//            SearchScreen()
//        }
    }

    @Test
    fun searchScreen_loadingIndicatorDisplayed_whenLoading() {
        (discoverMoviesUseCase as FakeDiscoverMoviesUseCase).setResponse(Result.success(emptyList()))
        composeTestRule.onNodeWithTag("loading_indicator",useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun searchScreen_whenMoviesLoadSuccessfully_displaysMovieList() {
        val fakeMovies = listOf(
            Movie(id = 1, title = "Inception", posterPath = "..jpg", voteAverage = 8.8),
            Movie(id = 2, title = "The Matrix", posterPath = "..jpg", voteAverage = 8.7)
        )
        (discoverMoviesUseCase as FakeDiscoverMoviesUseCase).setResponse(Result.success(fakeMovies))

        composeTestRule.onNodeWithText("Inception",useUnmergedTree = true).assertIsDisplayed()
//        composeTestRule.onNodeWithText("The Matrix").assertIsDisplayed()
//        composeTestRule.onNodeWithTag("loading_indicator").assertDoesNotExist()
    }
}