package com.galal.movies.screens.MovieListScreen.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.galal.movies.R
import com.galal.movies.data.api.ApiState
import com.galal.movies.model.Movie
import com.galal.movies.model.MovieDetail
import com.galal.movies.screens.MovieListScreen.viewModel.MovieViewModel
import com.galal.movies.util.networkListener
import com.galal.movies.utils.AppHeader
import com.galal.movies.utils.Constants
import com.galal.movies.utils.LoadingIndicator
import com.galal.movies.utils.NoInternetConnection
import com.galal.movies.utils.SliderWithIndicator

@Composable
fun MovieListScreen(navController: NavHostController, viewModel: MovieViewModel, onMovieClick: (Int) -> Unit) {
    val nowPlayingMovies = viewModel.nowPlayingMovies.collectAsState()
    val popularMovies = viewModel.popularMovies.collectAsState()
    val upcomingMovies = viewModel.upcomingMovies.collectAsState()
    val topRateMovies = viewModel.rateMovies.collectAsState()
    val getMovies = viewModel.getMovies.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val isNetworkAvailable = networkListener()
    //val showTapTarget = remember { mutableStateOf(true) }


    if (!isNetworkAvailable.value) {
        NoInternetConnection()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            AppHeader(navController = navController, title = stringResource(R.string.home))

            // Slider
            when (val state = getMovies.value) {
                is ApiState.Loading -> LoadingIndicator()
                is ApiState.Success -> SliderWithIndicator(movies = state.data, onMovieClick = onMovieClick)
                is ApiState.Failure -> NoInternetConnection()
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Layout
            val tabTitles = listOf(
                stringResource(R.string.now_playing),
                stringResource(R.string.popular),
                stringResource(R.string.upcoming),
                stringResource(R.string.top_rated_movies)
            )

            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                backgroundColor = Color.Black,
                contentColor = Color.White,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> MovieListContent(nowPlayingMovies.value, onMovieClick)
                1 -> MovieListContent(popularMovies.value, onMovieClick)
                2 -> MovieListContent(upcomingMovies.value, onMovieClick)
                3 -> MovieListContent(topRateMovies.value, onMovieClick)
            }
            /*
            *  when (selectedTabIndex) {
                0 -> MovieListContent(nowPlayingMovies.value, onMovieClick, showTapTarget.value,
                    onTapTargetDismiss = {showTapTarget.value = false})
                1 -> MovieListContent(popularMovies.value, onMovieClick, showTapTarget.value)
                2 -> MovieListContent(upcomingMovies.value, onMovieClick, showTapTarget.value)
                3 -> MovieListContent(topRateMovies.value, onMovieClick, showTapTarget.value)
            }*/
        }
    }
}

@Composable
fun MovieListContent(
    movieState: ApiState<List<Movie>>,
    onMovieClick: (Int) -> Unit,
    //showTapTarget:Boolean,
    //onTapTargetDismiss: () -> Unit ={}
    ) {
    when (movieState) {
        is ApiState.Loading -> LoadingIndicator()
        is ApiState.Failure -> NoInternetConnection()
        is ApiState.Success -> {
            LazyRow(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .fillMaxWidth()
            ) {
                items(movieState.data) { movie ->
                    MovieItem(movie = movie, onClick = onMovieClick)
                   /* if (showTapTarget) {
                        TapTargetOverlay(onDismiss = onTapTargetDismiss)
                    }*/
                }
            }
           /* if (showTapTarget) {
                TapTargetOverlay(onDismiss = onTapTargetDismiss)
            }*/
        }
    }
}


/*
@Composable
fun TapTargetOverlay(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xAA000000))
                .clickable { onDismiss() }
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Tap here to view movie details!",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Box(
                    modifier = Modifier
                        .size(230.dp, 150.dp)
                        .background(Color.Transparent)
                        .border(2.dp, Color.Yellow, shape = RoundedCornerShape(30.dp))
                )
            }
        }
    }
}
*/


@Composable
fun MovieItem(
    movie: Movie,
    onClick: (Int) -> Unit,

) {
    Card(
        modifier = Modifier
            .width(230.dp)
            .background(Color.White)
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 20.dp)
            .clickable { onClick(movie.id) },
        elevation = 10.dp,
        shape = RoundedCornerShape(30.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
             Image(
                 painter = rememberAsyncImagePainter(model = "${Constants.BASE_POSTER_IMAGE_URL}${movie.poster_path}"),
                 contentDescription = movie.title,
                 modifier = Modifier
                     .fillMaxWidth()
                     .clip(RoundedCornerShape(30.dp)),
                 contentScale = ContentScale.Crop
             )
            Text(
                movie.title,
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                modifier = Modifier.padding(top = 15.dp)
            )
            Text(
                text = stringResource(R.string.release, movie.release_date),
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 16.sp
                ),
                modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
            )
        }
    }
}



/*
@Composable
fun TapTargetView(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    targetContent: @Composable () -> Unit,
    hintText: String = "Tap here!"
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA000000))
                .clickable { onDismiss() }
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                targetContent()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = hintText,
                    color = Color.White,
                    style = MaterialTheme.typography.h1
                )
            }
        }
    }
}
*/

