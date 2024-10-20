package com.galal.movies.screens.Search.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galal.movies.data.api.ApiState
import com.galal.movies.data.repository.MovieRepository
import com.galal.movies.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _searchResults = MutableStateFlow<ApiState<List<Movie>>>(ApiState.Loading)
    val searchResults: StateFlow<ApiState<List<Movie>>> = _searchResults

    init {
        viewModelScope.launch { getAllMovies() }
    }

   suspend  fun getAllMovies() {
        viewModelScope.launch {
            _searchResults.value = ApiState.Loading

            val nowPlaying = repository.getNowPlayingMovies()
            val popular = repository.getPopularMovies()
            val upcoming = repository.getUpcomingMovies()

            if (nowPlaying is ApiState.Success && popular is ApiState.Success && upcoming is ApiState.Success) {
                val combinedMovies = nowPlaying.data.results + popular.data.results + upcoming.data.results
                _searchResults.value = ApiState.Success(combinedMovies)
            } else {
                _searchResults.value = ApiState.Failure("Failed to load all movies")
            }
        }
    }

    suspend fun searchMovies(query: String) {
        viewModelScope.launch {
            _searchResults.value = ApiState.Loading
            val response = repository.searchMovies(query)
            if (response is ApiState.Success) {
                _searchResults.value = ApiState.Success(response.data.results)
            } else if (response is ApiState.Failure) {
                _searchResults.value = ApiState.Failure(response.message)
            }
        }
    }
}