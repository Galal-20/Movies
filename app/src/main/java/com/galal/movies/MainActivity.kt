package com.galal.movies

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.galal.movies.data.api.ApiClient
import com.galal.movies.data.api.MovieApi
import com.galal.movies.data.repository.MovieRepositoryImp
import com.galal.movies.screens.MovieDetailScreen.view.MovieDetailScreen
import com.galal.movies.screens.MovieListScreen.view.MovieListScreen
import com.galal.movies.screens.MovieListScreen.viewModel.MovieViewModel
import com.galal.movies.ui.theme.MoviesTheme
import com.galal.movies.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Retrofit instance and Repository
        val movieApi: MovieApi = ApiClient.movieApi
        val movieRepository = MovieRepositoryImp(movieApi)
        val movieViewModel = MovieViewModel(movieRepository)

        setContent {
            MoviesTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    Scaffold { paddingValues ->
                        NavHost(navController = navController, startDestination = "movie_list", modifier = Modifier.padding(paddingValues))
                        {
                            composable("movie_list") {
                                MovieListScreen(viewModel = movieViewModel) { movieId ->
                                    navController.navigate("movie_detail/$movieId")
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}
