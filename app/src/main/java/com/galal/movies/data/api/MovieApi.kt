package com.galal.movies.data.api

import com.galal.movies.model.MovieDetail
import com.galal.movies.model.MovieResponse
import com.galal.movies.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("api_key") apiKey: String = Constants.API_KEY): MovieResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String = Constants.API_KEY): MovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("api_key") apiKey: String = Constants.API_KEY): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String): MovieDetail
}


