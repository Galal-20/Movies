package com.galal.movies.data.repository

import com.galal.movies.data.api.ApiState
import com.galal.movies.data.local.FavoriteMovieEntity
import com.galal.movies.model.Cast
import com.galal.movies.model.MovieDetail
import com.galal.movies.model.MovieResponse
import com.galal.movies.model.Video

interface MovieRepository {
    suspend fun getNowPlayingMovies(): ApiState<MovieResponse>
    suspend fun getPopularMovies(): ApiState<MovieResponse>
    suspend fun getUpcomingMovies(): ApiState<MovieResponse>
    suspend fun getMovieDetails(movieId: Int): ApiState<MovieDetail>
    suspend fun getMovieCredits(movieId: Int): ApiState<List<Cast>>
    suspend fun getSimilarMovies(movieId: String): ApiState<MovieResponse>
    suspend fun searchMovies(query: String): ApiState<MovieResponse>
    suspend fun getToRateMovies(): ApiState<MovieResponse>
    suspend fun getMovieVideos(movieId: Int): ApiState<List<Video>>

    suspend fun addFavoriteMovie(movie: FavoriteMovieEntity)
    suspend fun getFavoriteMovies(): List<FavoriteMovieEntity>
    suspend fun removeFavoriteMovie(movieId: Int)
}
