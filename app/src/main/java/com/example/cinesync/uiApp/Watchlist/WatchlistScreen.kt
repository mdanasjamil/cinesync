package com.example.cinesync.uiApp.Watchlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.cinesync.domain.Entity.Movie
import com.example.cinesync.uiApp.Search.AppFooter
import com.example.cinesync.uiApp.Search.MovieCard
import kotlinx.coroutines.flow.collect
import kotlin.time.Duration
import com.example.cinesync.R
import com.example.cinesync.ui.theme.CustomBlue

@Composable
fun TopBar(navController: NavController=rememberNavController(),
           modifier: Modifier=Modifier){
    Surface (
        modifier = modifier.fillMaxWidth(),
        color = CustomBlue
    ){
        Row (verticalAlignment = Alignment.CenterVertically){
            IconButton(onClick = {navController.popBackStack()})
            {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Text(
                text = "Watchlist",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 26.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun MovieListCard(
    onRemoveClicked: () -> Unit = {},
    movie: Movie,modifier: Modifier=Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 8.dp, bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp,
        color = Color.LightGray
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = "https://image.tmdb.org/t/p/w500${movie.posterPath}"),
                contentDescription = "Profile of ${movie.title}",
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = movie.title,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = String.format("%.1f", movie.voteAverage),
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
//            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {onRemoveClicked()},
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Remove",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun WatchlistScreen(modifier: Modifier = Modifier,
                    viewModel: WatchlistViewModel = hiltViewModel(),
                    navController: NavController = rememberNavController()
){
    val uiState by viewModel.uiState.collectAsState()
    val movies = uiState.movies
//    val movies = listOf<Movie>(
//        Movie(1, "Movie 1", "https://example.com/movie1.jpg", 8.5),
//        Movie(2, "Movie 2", "https://example.com/movie2.jpg", 7.2),
//        Movie(3, "Movie 3", "https://example.com/movie3.jpg", 6.8)
//    )

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect{message->
            snackbarHostState.showSnackbar(
                message =message,
                duration = SnackbarDuration.Short)
        }
    }

    Scaffold (
        topBar = {TopBar(navController)},
        bottomBar = { AppFooter(modifier, navController = navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(color = Color.Black)
            }
        } else if (!uiState.error) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(movies) { movie ->
                    MovieListCard(
                        movie = movie,
                        onRemoveClicked = { viewModel.removeMovieFromWatchlist(movie) })
                }
            }
        } else{
            Text(
                text = "Error in fetching Watchlist",
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun WatchlistScreenPreview(){
    WatchlistScreen()
}

