package com.galal.movies.screens.MovieListScreen.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.galal.movies.R
import com.galal.movies.data.api.ApiState
import com.galal.movies.model.Movie
import com.galal.movies.screens.MovieListScreen.viewModel.MovieViewModel
import com.galal.movies.utils.AppHeader
import com.galal.movies.utils.Constants
import com.galal.movies.utils.LoadingIndicator
import com.galal.movies.utils.ReusableLottie
import networkListener

@Composable
fun MovieListScreen(navController: NavHostController,viewModel: MovieViewModel, onMovieClick: (Int) -> Unit) {
    // Collect the movie lists as state
    val nowPlayingMovies = viewModel.nowPlayingMovies.collectAsState()
    val popularMovies = viewModel.popularMovies.collectAsState()
    val upcomingMovies = viewModel.upcomingMovies.collectAsState()


    // Fetch the movies
    LaunchedEffect(Unit) {
        viewModel.fetchNowPlayingMovies()
        viewModel.fetchPopularMovies()
        viewModel.fetchUpcomingMovies()
    }

    val isNetworkAvailable = networkListener()
    if (!isNetworkAvailable.value) {
        Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment
            .Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ReusableLottie(R.raw.no_internet, null, size = 400.dp, speed = 1f)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    stringResource(R.string.no_internet_connection),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center // Center the text
                )
            }
        }
    }else{
        LazyColumn(modifier = Modifier.fillMaxWidth().background(Color.White)) {
            item {
                AppHeader(navController = navController,title = stringResource(R.string.home))
            }

            // Slider with Now Playing Movies
            item {
                when (val state = nowPlayingMovies.value) {
                    is ApiState.Loading -> {
                        LoadingIndicator()
                    }
                    is ApiState.Success -> {
                        SliderWithIndicator(movies = state.data, onMovieClick = onMovieClick)
                    }
                    is ApiState.Failure -> {
                        Text(text = state.message, modifier = Modifier.padding(16.dp))
                    }
                }
            }

            // Now Playing Movies Section
            item {
                when (val state = nowPlayingMovies.value) {
                    is ApiState.Loading -> {
                        LoadingIndicator()
                    }
                    is ApiState.Success -> {
                        Text(text = stringResource(R.string.now_playing), fontSize = 18.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 18.dp, top = 16.dp, bottom = 8.dp))
                        LazyRow(
                            modifier = Modifier.padding(start =  16.dp)
                        ) {
                            items(state.data) { movie ->
                                MovieItem(movie = movie, onClick = onMovieClick)
                            }
                        }
                    }
                    is ApiState.Failure -> {
                        Text(text = state.message, modifier = Modifier.padding(16.dp))
                    }
                }
            }

            // Popular Movies Section
            item {
                when (val state = popularMovies.value) {
                    is ApiState.Loading -> {
                        LoadingIndicator()
                    }
                    is ApiState.Success -> {
                        Text(text = stringResource(R.string.popular), fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier
                            .padding(start = 18.dp, top = 16.dp, bottom = 8.dp))
                        LazyRow(
                            modifier = Modifier.padding(start =  16.dp)
                        ) {
                            items(state.data) { movie ->
                                MovieItem(movie = movie, onClick = onMovieClick)
                            }
                        }
                    }
                    is ApiState.Failure -> {
                        Text(text = state.message, modifier = Modifier.padding(16.dp))
                    }
                }
            }

            // Upcoming Movies Section
            item {
                when (val state = upcomingMovies.value) {
                    is ApiState.Loading -> {
                        LoadingIndicator()
                    }
                    is ApiState.Success -> {
                        Text(text = stringResource(R.string.upcoming), fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier
                            .padding(start = 18.dp, top = 16.dp, bottom = 8.dp))
                        LazyRow(
                            modifier = Modifier.padding(start =  16.dp)
                        ) {
                            items(state.data) { movie ->
                                MovieItem(movie = movie, onClick = onMovieClick)
                            }
                        }
                    }
                    is ApiState.Failure -> {
                        Text(text = state.message, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
            .clickable { onClick(movie.id) }
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = "${Constants.BASE_POSTER_IMAGE_URL}${movie.poster_path}"),
            contentDescription = movie.title,
            modifier = Modifier
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        Text(movie.title, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold, maxLines = 1)
        Text(text = stringResource(R.string.release, movie.release_date), style = MaterialTheme.typography.body2)
    }
}
