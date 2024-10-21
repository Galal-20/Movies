package com.galal.movies.screens.Search.view

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.galal.movies.util.networkListener
import com.galal.movies.utils.AppHeader
import com.galal.movies.utils.Constants
import com.galal.movies.utils.LoadingIndicator
import com.galal.movies.utils.NoInternetConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun SearchScreen(navController: NavHostController, viewModel: SearchViewModel = viewModel()) {
    var query by remember { mutableStateOf(TextFieldValue("")) }
    val searchResults by viewModel.searchResults.collectAsState()
    var isGridVisible by remember { mutableStateOf(false) }
    val isNetworkAvailable = networkListener()


    if (!isNetworkAvailable.value){
        NoInternetConnection()
    }
    else{
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {
            SearchHeader(navController)

            // Search Bar
            OutlinedTextField(
                value = query,
                onValueChange = { value ->
                    query = value
                    if (query.text.isNotEmpty()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.searchMovies(query.text)
                            isGridVisible = true
                        }
                    }else{
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.getAllMovies()
                        }

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                label = { Text(stringResource(R.string.search_movies), color = Color.Black) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    textColor = Color.Black,
                    cursorColor = Color.Black
                )
            )

            // Display Search Results
            when (searchResults) {
                is ApiState.Success -> {
                    val movies = (searchResults as ApiState.Success<List<Movie>>).data
                    if (movies.isNotEmpty()) {
                        MovieList(movies = movies, navController = navController)
                    } else {
                        MovieInfoMessage(isEmpty = true)
                    }
                }
                is ApiState.Loading -> LoadingIndicator()

                is ApiState.Failure -> NoInternetConnection()

            }
        }
    }
}

@Composable
private fun SearchHeader(navController: NavHostController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, start = 5.dp)
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.icon_back)
            )
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
}

@Composable
fun MovieList(movies: List<Movie>, navController: NavHostController) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(130.dp),
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth(),
        contentPadding = PaddingValues(15.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie = movie, navController = navController)
        }
    }
}


@Composable
fun MovieItem(movie: Movie, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("movie_detail/${movie.id}")
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = "${Constants.BASE_POSTER_IMAGE_URL}${movie.poster_path}"),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
        )
        Text(
            movie.title,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.release, movie.release_date),
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
fun MovieInfoMessage(isEmpty: Boolean) {
    val message = if (isEmpty) stringResource(R.string.error_message) else stringResource(R.string.error_message)
    Text(
            message,
            modifier = Modifier.padding(top = 17.dp, start = 27.dp),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface
        )

}

