package com.galal.movies.screens.MovieListScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galal.movies.data.api.ApiState
import com.galal.movies.data.repository.MovieRepositoryImp
import com.galal.movies.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepositoryImp) : ViewModel() {

    private val _nowPlayingMovies = MutableStateFlow<ApiState<List<Movie>>>(ApiState.Loading)
    val nowPlayingMovies: StateFlow<ApiState<List<Movie>>> = _nowPlayingMovies

    private val _popularMovies = MutableStateFlow<ApiState<List<Movie>>>(ApiState.Loading)
    val popularMovies: StateFlow<ApiState<List<Movie>>> = _popularMovies

    private val _upcomingMovies = MutableStateFlow<ApiState<List<Movie>>>(ApiState.Loading)
    val upcomingMovies: StateFlow<ApiState<List<Movie>>> = _upcomingMovies



    fun fetchNowPlayingMovies() {
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

    fun fetchPopularMovies() {
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

    fun fetchUpcomingMovies() {
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





}
