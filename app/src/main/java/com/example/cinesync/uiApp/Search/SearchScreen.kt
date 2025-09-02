package com.example.cinesync.uiApp.Search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.platform.testTag
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
import com.example.cinesync.ui.theme.CustomDarkGreen
import com.example.cinesync.uiApp.Footer.AppFooter
import com.example.cinesync.uiApp.Navigation.SharedViewModel
import kotlinx.coroutines.launch

@Composable
fun MovieCard(
    movie: Movie,
    onLocalAddClicked: () -> Unit,
    onGlobalAddClicked: () -> Unit,
    isMovieAddedLocally: Boolean,
    isMovieAddedGlobally: Boolean,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .weight(1f) // Use weight to share space instead of fillMaxWidth
                        .padding(bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isMovieAddedGlobally) {
                        Button(
                            onClick = { onGlobalAddClicked() },
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = CustomBlue),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(text = "+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = {},
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = CustomDarkGreen),
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

                Box(
                    modifier = Modifier
                        .weight(1f) // Use weight to share space instead of fillMaxWidth
                        .padding(bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isMovieAddedLocally) {
                        Button(
                            onClick = { onLocalAddClicked() },
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = CustomBlue),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(text = "+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = {},
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = CustomDarkGreen),
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

}


@Composable
fun SearchScreen(
    event:(SearchScreenEvent) -> Unit,
    uiState: SearchUiState,
    snackbarHostState: SnackbarHostState
) {

//    val snackbarHostState = remember { SnackbarHostState() }
//    val scope = rememberCoroutineScope()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TextField(
                value = uiState.searchQuery?:"",
                onValueChange = { searchQuery ->
                    event(SearchScreenEvent.SearchQueryChanged(searchQuery))
                    event(SearchScreenEvent.PerformSearch) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp).testTag("search_field"),
                label = { Text(stringResource(R.string.enter_your_movie)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                            event(SearchScreenEvent.SearchQueryChanged(uiState.searchQuery?:""))
                            event(SearchScreenEvent.PerformSearch)
                    }
                )
            )
        },
        bottomBar = {
            AppFooter(
                onSearchClick = { event(SearchScreenEvent.ScreenLaunched) },
                onLocalWatchlistClick = { event(SearchScreenEvent.LocalWatchlistClicked) },
                onGlobalWatchlistClick = { event(SearchScreenEvent.GlobalWatchlistClicked) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().testTag("loading_indicator")) {
                    CircularProgressIndicator(color = Color.Black)
                }
            } else if (!uiState.error) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize().testTag("movie_grid"),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    items(uiState.movies) { movie ->
                        MovieCard(
                            movie = movie,
                            onLocalAddClicked = {event(SearchScreenEvent.AddToLocalWatchlist(movie))},
                            onGlobalAddClicked = {event(SearchScreenEvent.AddToGlobalWatchlist(movie))},
                            isMovieAddedLocally = uiState.localWatchlistMovies.contains(movie),
                            isMovieAddedGlobally = uiState.globalWatchlistMovies.contains(movie)
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
    SearchScreen(
        event = {},
        uiState = SearchUiState(),
        snackbarHostState = SnackbarHostState()
    )
}