package com.example.cinesync.uiApp.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cinesync.uiApp.Login.LoginScreen
import com.example.cinesync.uiApp.Search.SearchScreen
import com.example.cinesync.uiApp.Signup.SignupScreen
import com.example.cinesync.uiApp.GlobalWatchlist.WatchlistScreen
import com.example.cinesync.uiApp.LocalWatchlist.LocalWatchlistScreen
import com.example.cinesync.uiApp.GlobalWatchlist.WatchlistScreen
import com.example.cinesync.uiApp.Login.addLoginAuthScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screens.LoginScreen.name,
        modifier = modifier,
    ) {
        composable(Screens.SearchScreen.name) {
            SearchScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
        composable(Screens.LocalWatchlistScreen.name) {
            LocalWatchlistScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
        composable(Screens.GlobalWatchlistScreen.name) {
            WatchlistScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
        addLoginAuthScreen(navController = navController,sharedViewModel = sharedViewModel)
        composable(Screens.SignupScreen.name) {
            SignupScreen(navController = navController)
        }
    }
}