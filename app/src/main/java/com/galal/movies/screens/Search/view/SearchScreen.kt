package com.galal.movies.screens.Search.view

import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.galal.movies.R
import com.galal.movies.data.api.ApiState
import com.galal.movies.model.Movie
import com.galal.movies.screens.Search.viewModel.SearchViewModel
import com.galal.movies.utils.Constants
import com.galal.movies.utils.ReusableLottie
import networkListener

@Composable
fun SearchScreen(navController: NavHostController, viewModel: SearchViewModel = viewModel()) {
    var query by remember { mutableStateOf(TextFieldValue("")) }
    var isGridVisible by remember { mutableStateOf(false) }

    val isNetworkAvailable = networkListener()
    if (!isNetworkAvailable.value){
        Box(modifier = Modifier.fillMaxSize().background(Color.White),
            contentAlignment = Alignment.Center) {
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
                    textAlign = TextAlign.Center
                )
            }
        }
    }else{
        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 5.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = stringResource(R.string.search),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 45.dp)
                )
            }

            // Search Bar
            OutlinedTextField(
                value = query,
                onValueChange = { value ->
                    query = value
                    if (query.text.isNotEmpty()) {
                        viewModel.searchMovies(query.text)
                        isGridVisible = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text("Search Movies") }
            )

            // Display Search Results
            val searchResults by viewModel.searchResults.collectAsState()
            when (searchResults) {
                is ApiState.Success -> {
                    val movies = (searchResults as ApiState.Success<List<Movie>>).data

                    if (movies.isNotEmpty()) {
                        MovieList(movies = movies, navController = navController)
                    } else {
                        MovieInfoMessage(isEmpty = true)
                    }
                }
                is ApiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is ApiState.Failure -> {
                    Text(
                        text = (searchResults as ApiState.Failure).message,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }



}

@Composable
fun MovieList(movies: List<Movie>, navController: NavHostController) {
    // Use LazyVerticalGrid to display items in a grid
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp), // Adjust based on desired item size
        modifier = Modifier.fillMaxSize().fillMaxWidth(),
        contentPadding = PaddingValues(30.dp),
        horizontalArrangement = Arrangement.spacedBy(50.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie = movie, navController = navController)
        }
    }
}


@Composable
fun MovieItem(movie: Movie, navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                // Navigate to MovieDetailScreen, pass the movie id
                navController.navigate("movie_detail/${movie.id}")
            },
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = "${Constants.BASE_POSTER_IMAGE_URL}${movie.poster_path}"),
            contentDescription = movie.title,
            modifier = Modifier
                .height(210.dp)
                .clip(RoundedCornerShape(60.dp))
        )
        Text(
            movie.title,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.release, movie.release_date),
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
fun MovieInfoMessage(isEmpty: Boolean) {
    val message = if (isEmpty) {
        "No Movies found. Try adjusting your search."
    } else {
        ""
    }
        Text(
            message,
            modifier = Modifier.padding(top = 17.dp, start = 27.dp),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface
        )

}
