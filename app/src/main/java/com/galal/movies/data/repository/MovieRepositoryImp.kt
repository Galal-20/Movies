package com.galal.movies.data.repository

import com.galal.movies.data.api.ApiState
import com.galal.movies.data.api.MovieApi
import com.galal.movies.data.local.AppDatabase
import com.galal.movies.data.local.FavoriteMovieDao
import com.galal.movies.data.local.FavoriteMovieEntity
import com.galal.movies.model.Cast
import com.galal.movies.model.MovieDetail
import com.galal.movies.model.MovieResponse
import com.galal.movies.model.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.withContext

class MovieRepositoryImp(
    private val movieApi: MovieApi,
    private val appDatabase: AppDatabase // Use Room database
): MovieRepository {
    private val favoriteMovieDao = appDatabase.favoriteMovieDao()

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

     override suspend fun getMovieDetails(movieId: Int): ApiState<MovieDetail> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = movieApi.getMovieDetails(movieId)
            ApiState.Success(response)
        } catch (e: Exception) {
            ApiState.Failure(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun getMovieCredits(movieId: Int): ApiState<List<Cast>> {
        return try {
            val response = movieApi.getMovieCredits(movieId)
            ApiState.Success(response.cast)
        } catch (e: Exception) {
            ApiState.Failure(e.message ?: "Unknown Error")
        }
    }

   override suspend fun getSimilarMovies(movieId: String): ApiState<MovieResponse> =
       withContext(Dispatchers.IO){
           return@withContext try {
               ApiState.Success(movieApi.getSimilarMovies(movieId.toInt()))
           }catch (e: Exception){
               ApiState.Failure(e.localizedMessage ?: "An unexpected error occurred")
           }
       }

    override suspend fun searchMovies(query: String): ApiState<MovieResponse> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = movieApi.searchMovies(query)
            ApiState.Success(response)
        } catch (e: Exception) {
            ApiState.Failure(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun getToRateMovies(): ApiState<MovieResponse> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = movieApi.getToRateMovies()
            ApiState.Success(response)
        } catch (e: Exception) {
            ApiState.Failure(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

     override suspend fun getMovieVideos(movieId: Int): ApiState<List<Video>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = movieApi.getMovieVideos(movieId)
            ApiState.Success(response.results.filter { it.site == "YouTube" && it.type == "Trailer" })
        } catch (e: Exception) {
            ApiState.Failure(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun addFavoriteMovie(movie: FavoriteMovieEntity) = withContext(Dispatchers.IO) {
        return@withContext try {
            favoriteMovieDao.insertFavorite(movie)
        }catch (e: Exception){
            throw e
        }
    }

    override suspend fun getFavoriteMovies(): List<FavoriteMovieEntity> = withContext(Dispatchers.IO) {
        return@withContext withContext(Dispatchers.IO) {
            favoriteMovieDao.getAllFavoriteMovies()
        }
    }

    override suspend fun removeFavoriteMovie(movieId: Int) = withContext(Dispatchers.IO) {
        return@withContext(Dispatchers.IO) {
            favoriteMovieDao.delete(movieId)
        }
    }


}

