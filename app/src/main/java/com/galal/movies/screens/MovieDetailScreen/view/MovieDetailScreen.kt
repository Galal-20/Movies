package com.galal.movies.screens.MovieDetailScreen.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.galal.movies.R
import com.galal.movies.data.api.ApiState
import com.galal.movies.model.Cast
import com.galal.movies.model.Movie
import com.galal.movies.model.MovieDetail
import com.galal.movies.model.MovieResponse
import com.galal.movies.screens.MovieDetailScreen.viewModel.DetailViewModel
import com.galal.movies.screens.MovieListScreen.viewModel.MovieViewModel
import com.galal.movies.utils.Constants
import com.galal.movies.utils.Constants.Companion.BASE_POSTER_IMAGE_URL
import com.galal.movies.utils.LoadingIndicator
import com.galal.movies.utils.ReusableLottie
import com.galal.movies.utils.netflixFamily
import networkListener

@Composable
fun MovieDetailScreen(viewModel: DetailViewModel, movieId: Int, navController: NavHostController) {
    val movieDetails = viewModel.movieDetails.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId)
        viewModel.fetchMovieCast(movieId)
        viewModel.fetchSimilarMovies(movieId.toString())
    }

    val isNetworkAvailable = networkListener()
    if (!isNetworkAvailable.value) {
        Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ReusableLottie(R.raw.no_internet, null, size = 400.dp, speed = 1f)
                Spacer(modifier = Modifier.height(10.dp))
                androidx.compose.material.Text(
                    stringResource(R.string.no_internet_connection),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center // Center the text
                )
            }
        }
    }else {
        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            when (val movie = movieDetails.value) {
                is ApiState.Loading -> {
                    LoadingIndicator()
                }

                is ApiState.Success -> {
                    MovieDetailContent(movie = movie.data, navController = navController, viewModel)
                }

                is ApiState.Failure -> {
                    Text(
                        text = movie.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun MovieDetailContent(movie: MovieDetail, navController: NavHostController, viewModel: DetailViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(0.dp)
    ) {
        // Poster Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            Image(painter = rememberAsyncImagePainter(Constants.BASE_BACKDROP_IMAGE_URL + movie!!.backdrop_path),
                contentDescription = "Backdrop Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .graphicsLayer {
                        alpha = 0.7f
                    })
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black), startY = 100f
                        )
                    )
            )
            Icon(
                imageVector = Icons.Default.ArrowBack,
                tint = Color.White,
                contentDescription = "",
                modifier = Modifier
                    .clickable {
                        navController.popBackStack()
                    }
                    .padding(24.dp)
            )
            Image(
                painter = rememberAsyncImagePainter(BASE_POSTER_IMAGE_URL + movie.poster_path),
                contentDescription = "Poster Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(160.dp)
                    .height(240.dp)
                    .offset(y = 75.dp)
                    .align(Alignment.Center)
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

        // Movie Title
        Text(
            text = movie.title,
            fontFamily = netflixFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Genres as Chips
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                movie.genres.forEach { genre ->
                    Chip(text = genre.name)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Movie Details Row (Release Date, Duration, Rating, Language)
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp)
            ) {
                val context = LocalContext.current
                MovieFieldDetails(stringResource(R.string.release_date), movie.release_date)
                MovieFieldDetails(stringResource(R.string.duration), movie?.runtime.toString() + stringResource(
                    R.string.min
                )
                )
                MovieFieldDetails(stringResource(R.string.rating), stringResource(R.string.Star) + String.format("%.1f", movie.vote_average))
                MovieFieldDetails(stringResource(R.string.language), movie.spoken_languages[0].name)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Movie Overview
        movie.overview?.let { OverviewSection(it, movie.tagline) }

        Spacer(modifier = Modifier.height(16.dp))

        // Movie Cast
        when (val castState = viewModel.movieCast.collectAsState().value) {
            is ApiState.Loading -> {
                LoadingIndicator()
            }
            is ApiState.Success -> {
                CastSection(castState.data)
            }
            is ApiState.Failure -> {
                Text(text = castState.message, color = MaterialTheme.colorScheme.error)
            }
        }

        // SimilarSection
        Spacer(modifier = Modifier.height(16.dp))

        val similarMovies = viewModel.similarMovies.collectAsState()
        when (val similarMovieState = similarMovies.value) {
            is ApiState.Loading -> {
                LoadingIndicator()
            }
            is ApiState.Success -> {
                SimilarMoviesSection(similarMovies = similarMovieState.data, navController = navController)
            }
            is ApiState.Failure -> {
                Text(
                    text = similarMovieState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun Chip(text: String) {
    Card(
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(2.dp, Color.Black),
        modifier = Modifier.padding(end = 0.dp)
    ) {
        Text(
            text = text,
            fontFamily = netflixFamily,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(8.dp),
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@Composable
private fun MovieFieldDetails(name: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = name,
            fontFamily = netflixFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 1.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Text(
            text = value,
            fontFamily = netflixFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp),
        )
    }
}

@Composable
fun OverviewSection(overview: String, tagline: String?) {
    Column {
        Text(
            modifier = Modifier.padding(horizontal = 22.dp),
            text = "Overview",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,color = Color.Black
        )
        Text(
            modifier = Modifier.padding(horizontal = 25.dp),
            text = tagline!!,
            fontFamily = netflixFamily,
            fontSize = 17.sp,
            fontStyle = FontStyle.Italic,
            lineHeight = 16.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            modifier = Modifier.padding(horizontal = 25.dp),
            text = overview,
            fontFamily = netflixFamily,
            fontSize = 16.sp,
            color = Color.Black,
            lineHeight = 16.sp
        )
    }
}

@Composable
fun CastSection(castList: List<Cast>) {
    Column(modifier = Modifier.padding(horizontal = 22.dp)) {
        Text(text = "Cast", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            castList.forEach { cast ->
                CastMemberItem(cast)
            }
        }
    }
}


@Composable
fun CastMemberItem(cast: Cast) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(120.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        if (cast.profile_path != null) {
            Image(
                painter = rememberAsyncImagePainter(model = BASE_POSTER_IMAGE_URL + cast.profile_path),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Cast Member Placeholder",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(4.dp))
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 5.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = cast.name, style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White,
                    fontFamily = netflixFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
            )
        }
    }
}

@Composable
fun SimilarMoviesSection(similarMovies: List<Movie>, navController: NavHostController) {
    Column(modifier = Modifier.padding(horizontal = 22.dp)) {
        Text(text = "Similar Movies", fontWeight = FontWeight.Bold, fontSize = 18.sp,color = Color.Black)
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            similarMovies.forEach { movie ->
                SimilarMovieItem(movie, navController)
            }
        }
    }
}

@Composable
fun SimilarMovieItem(movie: Movie, navController: NavHostController) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(120.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                navController.navigate("movie_detail/${movie.id}")
            }
    ) {
        if (movie.poster_path != null) {
            Image(
                painter = rememberAsyncImagePainter(BASE_POSTER_IMAGE_URL + movie.poster_path),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Movie Poster Placeholder",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(4.dp))
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 5.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = movie.title, style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White,
                    fontFamily = netflixFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
            )
        }
    }
}




