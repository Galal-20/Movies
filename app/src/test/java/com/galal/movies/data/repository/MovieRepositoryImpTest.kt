package com.galal.movies.data.repository


import com.galal.movies.data.api.ApiState
import com.galal.movies.data.api.MovieApi
import com.galal.movies.model.Cast
import com.galal.movies.model.CastResponse
import com.galal.movies.model.MovieDetail
import com.galal.movies.model.MovieResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryImpTest {

    private lateinit var movieRepository: MovieRepositoryImp
    private val movieApi: MovieApi = mockk()

    @Before
    fun setUp() {
        movieRepository = MovieRepositoryImp(movieApi)
    }

    @Test
    fun `getNowPlayingMovies success returns Success`() = runTest {
        val nowPlayingMovies = MovieResponse(results = listOf())
        coEvery { movieApi.getNowPlayingMovies() } returns nowPlayingMovies
        val result = movieRepository.getNowPlayingMovies()
        assertEquals(ApiState.Success(nowPlayingMovies), result)
        coVerify(exactly = 1) { movieApi.getNowPlayingMovies() }
    }

    @Test
    fun `getNowPlayingMovies failure returns Failure`() = runTest {
        val errorMessage = "An unexpected error occurred"
        coEvery { movieApi.getNowPlayingMovies() } throws Exception(errorMessage)
        val result = movieRepository.getNowPlayingMovies()
        assertEquals(ApiState.Failure(errorMessage), result)
        coVerify(exactly = 1) { movieApi.getNowPlayingMovies() }
    }

    @Test
    fun `getPopularMovies success returns Success`() = runTest {
        val popularMovies = MovieResponse(results = listOf())
        coEvery { movieApi.getPopularMovies() } returns popularMovies
        val result = movieRepository.getPopularMovies()
        assertEquals(ApiState.Success(popularMovies), result)
        coVerify(exactly = 1) { movieApi.getPopularMovies() }
    }

    @Test
    fun `getPopularMovies failure returns Failure`() = runTest {
        val errorMessage = "An unexpected error occurred"
        coEvery { movieApi.getPopularMovies() } throws Exception(errorMessage)
        val result = movieRepository.getPopularMovies()
        assertEquals(ApiState.Failure(errorMessage), result)
        coVerify(exactly = 1) { movieApi.getPopularMovies() }
    }

    @Test
    fun `getUpcomingMovies success returns Success`() = runTest {
        val upcomingMovies = MovieResponse(results = listOf())
        coEvery { movieApi.getUpcomingMovies() } returns upcomingMovies
        val result = movieRepository.getUpcomingMovies()
        assertEquals(ApiState.Success(upcomingMovies), result)
        coVerify(exactly = 1) { movieApi.getUpcomingMovies() }
    }

    @Test
    fun `getUpcomingMovies failure returns Failure`() = runTest {
        val errorMessage = "An unexpected error occurred"
        coEvery { movieApi.getUpcomingMovies() } throws Exception(errorMessage)
        val result = movieRepository.getUpcomingMovies()
        assertEquals(ApiState.Failure(errorMessage), result)
        coVerify(exactly = 1) { movieApi.getUpcomingMovies() }
    }

    @Test
    fun `getMovieDetails success returns Success`() = runTest {
        val movieDetail = MovieDetail(
            id = 1, title = "Movie Detail", overview = "Movie overview",
            genres = emptyList(),runtime = 120, release_date = "2023-09-11",
            spoken_languages = emptyList()
        )
        coEvery { movieApi.getMovieDetails(1) } returns movieDetail
        val result = movieRepository.getMovieDetails(1)
        assertEquals(ApiState.Success(movieDetail), result)
        coVerify(exactly = 1) { movieApi.getMovieDetails(1) }
    }

    @Test
    fun `getMovieDetails failure returns Failure`() = runTest {
        val errorMessage = "An unexpected error occurred"
        coEvery { movieApi.getMovieDetails(1) } throws Exception(errorMessage)
        val result = movieRepository.getMovieDetails(1)
        assertEquals(ApiState.Failure(errorMessage), result)
        coVerify(exactly = 1) { movieApi.getMovieDetails(1) }
    }

    @Test
    fun `getMovieCredits success returns Success`() = runTest {
        val castList = listOf(Cast(id = 1, name = "Actor", profile_path = "profile.jpg"))
        coEvery { movieApi.getMovieCredits(1) } returns CastResponse(cast = castList)
        val result = movieRepository.getMovieCredits(1)
        assertEquals(ApiState.Success(castList), result)
        coVerify(exactly = 1) { movieApi.getMovieCredits(1) }
    }

    @Test
    fun `getMovieCredits failure returns Failure`() = runTest {
        val errorMessage = "An unexpected error occurred"
        coEvery { movieApi.getMovieCredits(1) } throws Exception(errorMessage)
        val result = movieRepository.getMovieCredits(1)
        assertEquals(ApiState.Failure(errorMessage), result)
        coVerify(exactly = 1) { movieApi.getMovieCredits(1) }
    }

    @Test
    fun `searchMovies success returns Success`() = runTest {
        val searchMovies = MovieResponse(results = listOf())
        coEvery { movieApi.searchMovies("query") } returns searchMovies
        val result = movieRepository.searchMovies("query")
        assertEquals(ApiState.Success(searchMovies), result)
        coVerify(exactly = 1) { movieApi.searchMovies("query") }
    }

    @Test
    fun `searchMovies failure returns Failure`() = runTest {
        val errorMessage = "An unexpected error occurred"
        coEvery { movieApi.searchMovies("query") } throws Exception(errorMessage)
        val result = movieRepository.searchMovies("query")
        assertEquals(ApiState.Failure(errorMessage), result)
        coVerify(exactly = 1) { movieApi.searchMovies("query") }
    }

    @Test
    fun `getSimilarMovies success returns Success`() = runTest {
        val similarMovies = MovieResponse(results = listOf())
        coEvery { movieApi.getSimilarMovies(1) } returns similarMovies
        val result = movieRepository.getSimilarMovies("1")
        assertEquals(ApiState.Success(similarMovies), result)
        coVerify(exactly = 1) { movieApi.getSimilarMovies(1) }
    }

    @Test
    fun `getSimilarMovies failure returns Failure`() = runTest {
        val errorMessage = "An unexpected error occurred"
        coEvery { movieApi.getSimilarMovies(1) } throws Exception(errorMessage)
        val result = movieRepository.getSimilarMovies("1")
        assertEquals(ApiState.Failure(errorMessage), result)
        coVerify(exactly = 1) { movieApi.getSimilarMovies(1) }
    }

    @Test
    fun `getToRateMovies success returns Success`() = runTest {
        val toRateMovies = MovieResponse(results = listOf())
        coEvery { movieApi.getToRateMovies() } returns toRateMovies
        val result = movieRepository.getToRateMovies()
        assertEquals(ApiState.Success(toRateMovies), result)
        coVerify(exactly = 1) { movieApi.getToRateMovies() }
    }

    @Test
    fun `getToRateMovies failure returns Failure`() = runTest {
        val errorMessage = "An unexpected error occurred"
        coEvery { movieApi.getToRateMovies() } throws Exception(errorMessage)
        val result = movieRepository.getToRateMovies()
        assertEquals(ApiState.Failure(errorMessage), result)
        coVerify(exactly = 1) { movieApi.getToRateMovies() }
    }
}
