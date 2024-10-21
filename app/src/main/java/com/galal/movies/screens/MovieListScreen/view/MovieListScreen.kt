package com.galal.movies.screens.MovieListScreen.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.galal.movies.R
import com.galal.movies.data.api.ApiState
import com.galal.movies.model.Movie
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
    var selectedTabIndex by remember { mutableIntStateOf(0) }


    LaunchedEffect(Unit) {
        viewModel.fetchNowPlayingMovies()
        viewModel.fetchPopularMovies()
        viewModel.fetchUpcomingMovies()
        viewModel.fetchToRateMovies()
    }

    val isNetworkAvailable = networkListener()
    if (!isNetworkAvailable.value) {
        //NoInternetConnection()
    } else
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            AppHeader(navController = navController, title = stringResource(R.string.home))

            //Spacer(modifier = Modifier.height(0.dp))
            // Slider for Upcoming Movies
            when (val state = upcomingMovies.value) {
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
        }
    }

}

@Composable
fun MovieListContent(movieState: ApiState<List<Movie>>, onMovieClick: (Int) -> Unit) {
    when (movieState) {
        is ApiState.Loading -> LoadingIndicator()
        is ApiState.Failure -> ""
        is ApiState.Success -> {
            LazyRow(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .fillMaxWidth()
            ) {
                items(movieState.data) { movie ->
                    MovieItem(movie = movie, onClick = onMovieClick)
                }
            }
        }
    }
}


@Composable
fun MovieItem(movie: Movie, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .width(230.dp).background(Color.White)
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
