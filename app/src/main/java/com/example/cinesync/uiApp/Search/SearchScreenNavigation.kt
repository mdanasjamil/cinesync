package com.example.cinesync.uiApp.Search

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.cinesync.uiApp.Navigation.Screens
import com.example.cinesync.uiApp.Navigation.SharedViewModel
import com.example.cinesync.uiApp.Signup.SignupViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun NavGraphBuilder.addSearchScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    composable(Screens.SearchScreen.name) {
        val viewModel = hiltViewModel<SearchViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
        val user by sharedViewModel.currentUser.collectAsState()

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = Unit) {
            viewModel.onEvent(user, SearchScreenEvent.ScreenLaunched)
        }

        LaunchedEffect(lifecycleOwner) {
            lifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                withContext(context = Dispatchers.Main.immediate) {
                    viewModel.action.collectLatest { action ->
                        when (action) {
                            is SearchScreenAction.ShowSnackbar -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar(message = action.message)
                                }
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }

        SearchScreen(
            uiState = uiState,
            event = {event->
                when(event){
                    is SearchScreenEvent.GlobalWatchlistClicked -> {
                        navController.navigate(Screens.GlobalWatchlistScreen.name)
                    }
                    is SearchScreenEvent.LocalWatchlistClicked -> {
                        navController.navigate(Screens.LocalWatchlistScreen.name)
                    }
                    else -> viewModel.onEvent(user, event)
                }
            },
            snackbarHostState = snackbarHostState
        )
    }
}