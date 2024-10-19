package com.galal.movies.data.repository

import com.galal.movies.data.api.ApiState
import com.galal.movies.model.Movie
import com.galal.movies.model.MovieResponse

interface MovieRepository {
    suspend fun getNowPlayingMovies(): ApiState<MovieResponse>
    suspend fun getPopularMovies(): ApiState<MovieResponse>
    suspend fun getUpcomingMovies(): ApiState<MovieResponse>
}
