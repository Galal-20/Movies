package com.galal.movies.data.repository

import com.galal.movies.data.api.ApiState
import com.galal.movies.data.api.MovieApi
import com.galal.movies.model.Movie
import com.galal.movies.model.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepositoryImp(private val movieApi: MovieApi): MovieRepository {

    override suspend fun getNowPlayingMovies(): ApiState<MovieResponse> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = movieApi.getNowPlayingMovies()
            ApiState.Success(response)
        } catch (e: Exception) {
            ApiState.Failure(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun getPopularMovies(): ApiState<MovieResponse> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = movieApi.getPopularMovies()
            ApiState.Success(response)
        } catch (e: Exception) {
            ApiState.Failure(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun getUpcomingMovies(): ApiState<MovieResponse> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = movieApi.getUpcomingMovies()
            ApiState.Success(response)
        } catch (e: Exception) {
            ApiState.Failure(e.localizedMessage ?: "An unexpected error occurred")
        }
    }


}

