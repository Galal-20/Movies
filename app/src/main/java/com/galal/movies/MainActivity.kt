package com.galal.movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.galal.movies.data.api.ApiClient
import com.galal.movies.data.local.AppDatabase
import com.galal.movies.data.repository.MovieRepositoryImp
import com.galal.movies.screens.FavouriteScreen.view.FavouriteScreen
import com.galal.movies.screens.FavouriteScreen.viewModel.FavouriteViewModel
import com.galal.movies.screens.FavouriteScreen.viewModel.FavouriteViewModelFactory
import com.galal.movies.screens.MovieDetailScreen.view.MovieDetailScreen
import com.galal.movies.screens.MovieDetailScreen.viewModel.DetailViewModel
import com.galal.movies.screens.MovieDetailScreen.viewModel.DetailViewModelFactory
import com.galal.movies.screens.MovieListScreen.view.MovieListScreen
import com.galal.movies.screens.MovieListScreen.viewModel.MovieViewModel
import com.galal.movies.screens.MovieListScreen.viewModel.MovieViewModelFactory
import com.galal.movies.screens.Search.view.SearchScreen
import com.galal.movies.screens.Search.viewModel.SearchViewModel
import com.galal.movies.screens.Search.viewModel.SearchViewModelFactory
import com.galal.movies.screens.Splash.SplashScreen
import com.galal.movies.ui.theme.MoviesTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appDatabase = AppDatabase.getDatabase(applicationContext)

        val movieViewModel: MovieViewModel by viewModels {
            MovieViewModelFactory(MovieRepositoryImp(ApiClient.movieApi, appDatabase))
        }
        val movieDetailViewModel: DetailViewModel by viewModels {
            DetailViewModelFactory(MovieRepositoryImp(ApiClient.movieApi, appDatabase))
        }
        val searchViewModel: SearchViewModel by viewModels {
            SearchViewModelFactory(MovieRepositoryImp(ApiClient.movieApi, appDatabase))
        }
        val favouriteViewModel: FavouriteViewModel by viewModels {
            FavouriteViewModelFactory(MovieRepositoryImp(ApiClient.movieApi, appDatabase))
        }

        setContent {
            MoviesTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    Scaffold { paddingValues ->
                        NavHost(navController = navController, startDestination = "splash_screen", modifier = Modifier.padding(paddingValues)) {
                            composable("splash_screen") {
                                SplashScreen(navHostController = navController)
                            }
                            composable("movie_list") {
                                MovieListScreen(viewModel = movieViewModel, navController = navController) { movieId ->
                                    navController.navigate("movie_detail/$movieId")
                                }
                            }
                            composable("movie_detail/{movieId}") { backStackEntry ->
                                val movieId = backStackEntry.arguments?.getString("movieId")?.toInt() ?: -1
                                MovieDetailScreen(
                                    viewModel = movieDetailViewModel,
                                    movieId = movieId, navController
                                )
                            }
                            composable("search_screen") {
                                SearchScreen(navController, searchViewModel)
                            }
                            composable("favourite_screen") {
                                FavouriteScreen(navController, favouriteViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
