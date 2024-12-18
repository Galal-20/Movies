package com.galal.movies.screens.MovieDetailScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galal.movies.data.api.ApiState
import com.galal.movies.data.local.FavoriteMovieEntity
import com.galal.movies.data.repository.MovieRepository
import com.galal.movies.model.Cast
import com.galal.movies.model.Movie
import com.galal.movies.model.MovieDetail
import com.galal.movies.model.Video
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: MovieRepository): ViewModel() {
    private val _movieDetails = MutableStateFlow<ApiState<MovieDetail>>(ApiState.Loading)
    val movieDetails: StateFlow<ApiState<MovieDetail>> = _movieDetails

    private val _movieCast = MutableStateFlow<ApiState<List<Cast>>>(ApiState.Loading)
    val movieCast: StateFlow<ApiState<List<Cast>>> = _movieCast

    private val _similarMovies = MutableStateFlow<ApiState<List<Movie>>>(ApiState.Loading)
    val similarMovies: StateFlow<ApiState<List<Movie>>> = _similarMovies

    private val _movieVideos = MutableStateFlow<ApiState<List<Video>>>(ApiState.Loading)
    val movieVideos: StateFlow<ApiState<List<Video>>> = _movieVideos


   suspend fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _movieDetails.value = ApiState.Loading
            val response = repository.getMovieDetails(movieId)
            _movieDetails.value = response
        }
    }

   suspend fun fetchMovieCast(movieId: Int) {
        viewModelScope.launch {
            _movieCast.value = ApiState.Loading
            val response = repository.getMovieCredits(movieId)
            _movieCast.value = response
        }
    }

    suspend fun fetchSimilarMovies(movieId: String) {
        viewModelScope.launch {
            _similarMovies.value = ApiState.Loading
            val response = repository.getSimilarMovies(movieId)
            if (response is ApiState.Success) {
                _similarMovies.value = ApiState.Success(response.data.results)
            }else if (response is ApiState.Failure) {
                _similarMovies.value = ApiState.Failure(response.message)
            }
        }
    }

   suspend fun fetchMovieVideos(movieId: Int) {
        viewModelScope.launch {
            _movieVideos.value = ApiState.Loading
            val response = repository.getMovieVideos(movieId)
            _movieVideos.value = response
        }
    }

    fun addToFavorite(movie: MovieDetail) {
        viewModelScope.launch {
            val favoriteMovie = FavoriteMovieEntity(
                id = movie.id,
                title = movie.title,
                posterPath = movie.poster_path ?: "",
                releaseDate = movie.release_date,
                voteAverage = movie.vote_average!!,
                movie.overview,
                movie.runtime,
                movie.backdrop_path ?: "",
                movie.tagline ?: "",
                movie.genres.joinToString { it.name },
                movie.spoken_languages.joinToString { it.name }
            )
            repository.addFavoriteMovie(favoriteMovie)
        }
    }
}