package com.galal.movies.screens.FavouriteScreen.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galal.movies.data.local.FavoriteMovieEntity
import com.galal.movies.data.repository.MovieRepository
import kotlinx.coroutines.launch

class FavouriteViewModel(private val repository: MovieRepository):ViewModel() {


    private val _favoriteMovies = MutableLiveData<List<FavoriteMovieEntity>>()
    val favoriteMovies: LiveData<List<FavoriteMovieEntity>> = _favoriteMovies

    init {
        viewModelScope.launch {
            getFavoriteMovies()
        }
    }

    suspend fun getFavoriteMovies() {
        viewModelScope.launch {
            _favoriteMovies.value = repository.getFavoriteMovies()
        }
    }

     fun deleteMovie(movieId: Int) {
        viewModelScope.launch {
            repository.removeFavoriteMovie(movieId)
            getFavoriteMovies() // Refresh the list after deletion
        }
    }
}



/*fun deleteMovie(movieId: Int) {
        viewModelScope.launch {
            repository.removeFavoriteMovie(movieId)
            getFavoriteMovies() // Refresh the list after deletion
        }
    }*/
/*// LiveData to hold the list of favorite movies
    private val _favoriteMovies = mutableStateOf<List<FavoriteMovieEntity>>(emptyList())
    val favoriteMovies: State<List<FavoriteMovieEntity>> = _favoriteMovies

    init {
        getFavoriteMovies()
    }

    private fun getFavoriteMovies() {
        viewModelScope.launch {
            _favoriteMovies.value = repository.getFavoriteMovies()
        }
    }

    *//*fun removeFavoriteMovie(movieId: Int) {
        viewModelScope.launch {
            repository.removeFavoriteMovie(movieId)
            getFavoriteMovies() // Refresh the list after deletion
        }
    }*/