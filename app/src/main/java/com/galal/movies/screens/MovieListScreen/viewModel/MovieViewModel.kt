package com.galal.movies.screens.MovieListScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galal.movies.data.api.ApiState
import com.galal.movies.data.local.FavoriteMovieEntity
import com.galal.movies.data.repository.MovieRepository
import com.galal.movies.data.repository.MovieRepositoryImp
import com.galal.movies.model.Movie
import com.galal.movies.model.MovieDetail
import com.galal.movies.util.networkListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _nowPlayingMovies = MutableStateFlow<ApiState<List<Movie>>>(ApiState.Loading)
    val nowPlayingMovies: StateFlow<ApiState<List<Movie>>> = _nowPlayingMovies

    private val _popularMovies = MutableStateFlow<ApiState<List<Movie>>>(ApiState.Loading)
    val popularMovies: StateFlow<ApiState<List<Movie>>> = _popularMovies

    private val _upcomingMovies = MutableStateFlow<ApiState<List<Movie>>>(ApiState.Loading)
    val upcomingMovies: StateFlow<ApiState<List<Movie>>> = _upcomingMovies

    private val _rateMovies = MutableStateFlow<ApiState<List<Movie>>>(ApiState.Loading)
    val rateMovies: StateFlow<ApiState<List<Movie>>> = _rateMovies

    private val _getMovies = MutableStateFlow<ApiState<List<Movie>>>(ApiState.Loading)
    val getMovies: StateFlow<ApiState<List<Movie>>> = _getMovies





    init {
        viewModelScope.launch {
            fetchNowPlayingMovies()
            fetchPopularMovies()
            fetchUpcomingMovies()
            fetchToRateMovies()
            fetchMovies()
        }

    }


    suspend fun fetchNowPlayingMovies() {
        viewModelScope.launch {
            _nowPlayingMovies.value = ApiState.Loading
            val response = repository.getNowPlayingMovies()
            if (response is ApiState.Success) {
                _nowPlayingMovies.value = ApiState.Success(response.data.results)
            } else if (response is ApiState.Failure) {
                _nowPlayingMovies.value = ApiState.Failure(response.message)
            }
        }
    }

    suspend fun fetchPopularMovies() {
        viewModelScope.launch {
            _popularMovies.value = ApiState.Loading
            val response = repository.getPopularMovies()
            if (response is ApiState.Success) {
                _popularMovies.value = ApiState.Success(response.data.results)
            } else if (response is ApiState.Failure) {
                _popularMovies.value = ApiState.Failure(response.message)
            }
        }
    }

   suspend fun fetchUpcomingMovies() {
        viewModelScope.launch {
            _upcomingMovies.value = ApiState.Loading
            val response = repository.getUpcomingMovies()
            if (response is ApiState.Success) {
                _upcomingMovies.value = ApiState.Success(response.data.results)
            } else if (response is ApiState.Failure) {
                _upcomingMovies.value = ApiState.Failure(response.message)
            }
        }
    }

    suspend fun fetchToRateMovies() {
        viewModelScope.launch {
            _rateMovies.value = ApiState.Loading
            val response = repository.getToRateMovies()
            if (response is ApiState.Success) {
                _rateMovies.value = ApiState.Success(response.data.results)
            }else if (response is ApiState.Failure) {
                _rateMovies.value = ApiState.Failure(response.message)
            }
        }
    }


    suspend fun fetchMovies() {
        viewModelScope.launch {
            _getMovies.value = ApiState.Loading
            val nowPlaying = repository.getNowPlayingMovies()
            val popular = repository.getPopularMovies()
            val upcoming = repository.getUpcomingMovies()
            val topRate = repository.getToRateMovies()

            if (nowPlaying is ApiState.Success &&
                popular is ApiState.Success &&
                upcoming is ApiState.Success &&
                topRate is ApiState.Success) {
                val combinedMovies = nowPlaying.data.results + popular.data.results + upcoming
                    .data.results + topRate.data.results
                _getMovies.value = ApiState.Success(combinedMovies)
            } else {
                _getMovies.value = ApiState.Failure("Failed to load all movies")
            }
        }
    }


}
