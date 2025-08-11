package com.example.cinesync.uiApp.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cinesync.uiApp.Search.SearchScreen
import com.example.cinesync.uiApp.Watchlist.WatchlistScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.SearchScreen.name,
        modifier = modifier,
    ){
        composable(Screens.SearchScreen.name){
            SearchScreen(navController=navController)
        }
        composable(Screens.WatchlistScreen.name){
            WatchlistScreen(navController=navController)
        }

    }
}