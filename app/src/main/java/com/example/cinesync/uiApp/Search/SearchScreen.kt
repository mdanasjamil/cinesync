package com.example.cinesync.uiApp.Search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.cinesync.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.uiApp.Navigation.Screens
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cinesync.ui.theme.CustomBlue
import com.example.cinesync.uiApp.Watchlist.WatchlistViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MovieCard(
    movie: Movie,
    onAddClicked: () -> Unit,
    isMovieAdded: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.width(180.dp).padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 4.dp,
        color = Color.LightGray
    ) {
        Column {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop // This makes the image fit beautifully
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = movie.title,
                    modifier = Modifier.weight(1f),
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = String.format("%.1f", movie.voteAverage),
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Black,
                    fontWeight = FontWeight.Light
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                if(!isMovieAdded) {
                    Button(
                        onClick = { onAddClicked() },
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = "+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }else{
                    Button(
                        onClick = {},
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Check",
                            tint = Color.White,
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun AppFooter(modifier: Modifier = Modifier,navController: NavController) {
    BottomAppBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = Color.White,
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search Button
            Button(onClick = { /*TODO Fix search query search button and normal search button*/
                navController.navigate(Screens.SearchScreen.name) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomBlue,
                    contentColor = Color.White)
                ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
                Text(text = "Search", modifier = Modifier.padding(start = 8.dp))
            }

            // Watchlist Button
            Button(onClick = {navController.navigate(Screens.WatchlistScreen.name)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomBlue,
                    contentColor = Color.White)
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Watchlist")
                Text(text = "Watchlist", modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    navController: NavController = rememberNavController()
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val movies = uiState.movies

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchMovies()
        viewModel.observeWatchlistChanges()
    }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message = message,duration = SnackbarDuration.Short)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it
                        viewModel.fetchSearchedMovie(searchQuery)  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text(stringResource(R.string.enter_your_movie)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.fetchSearchedMovie(searchQuery)
                    }
                )
            )
        },
        bottomBar = {
            AppFooter(navController=navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(color = Color.Black)
                }
            } else if (!uiState.error) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    items(movies) { movie ->
                        MovieCard(
                            movie = movie,
                            onAddClicked = { viewModel.addMovieToWatchlist(movie) },
                            isMovieAdded = uiState.watchlistMovies.contains(movie)
                        )
                    }
                }
            } else {
                Text(
                    text = "Error in fetching movies",
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun SearchScreenPreview(){
    SearchScreen()
}