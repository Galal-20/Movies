package com.galal.movies.screens.MovieListScreen.viewModel

import com.galal.movies.data.api.ApiState
import com.galal.movies.data.repository.MovieRepository
import com.galal.movies.model.Movie
import com.galal.movies.model.MovieResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieViewModelTest {

    private lateinit var viewModel: MovieViewModel
    private val movieRepository: MovieRepository = mockk()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MovieViewModel(movieRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchNowPlayingMovies success updates state to Success`() = runTest {
        // Arrange: Set up the expected result
        val expectedMovies = listOf(
            Movie(id = 1, title = "Movie 1","1111", "aaaa"),
            Movie(id = 2, title = "Movie 2", "2222", "bbbb"))
        val movieResponse = MovieResponse(results = expectedMovies)
        coEvery { movieRepository.getNowPlayingMovies() } returns ApiState.Success(movieResponse)
        viewModel.fetchNowPlayingMovies()
        val state = viewModel.nowPlayingMovies.first()
        assertEquals(ApiState.Success(expectedMovies), state)
        coVerify { movieRepository.getNowPlayingMovies() }
    }

    @Test
    fun `fetchNowPlayingMovies failure updates state to Failure`() = runTest {
        val errorMessage = "Network Error"
        coEvery { movieRepository.getNowPlayingMovies() } returns ApiState.Failure(errorMessage)
        viewModel.fetchNowPlayingMovies()
        val state = viewModel.nowPlayingMovies.first()
        assertEquals(ApiState.Failure(errorMessage), state)
        coVerify { movieRepository.getNowPlayingMovies() }
    }

    @Test
    fun `fetchPopularMovies success updates state to Success`() = runTest {
        val expectedMovies = listOf(Movie(
            id = 3, title = "Popular Movie 1", "3333", "cccc"),
            Movie(id = 4, title = "Popular Movie 2","4444", "dddd"))
        val movieResponse = MovieResponse(results = expectedMovies)
        coEvery { movieRepository.getPopularMovies() } returns ApiState.Success(movieResponse)
        viewModel.fetchPopularMovies()
        val state = viewModel.popularMovies.first()
        assertEquals(ApiState.Success(expectedMovies), state)
        coVerify { movieRepository.getPopularMovies() }
    }

    @Test
    fun `fetchPopularMovies failure updates state to Failure`() = runTest {
        val errorMessage = "Failed to fetch popular movies"
        coEvery { movieRepository.getPopularMovies() } returns ApiState.Failure(errorMessage)
        viewModel.fetchPopularMovies()
        val state = viewModel.popularMovies.first()
        assertEquals(ApiState.Failure(errorMessage), state)
        coVerify { movieRepository.getPopularMovies() }
    }

    @Test
    fun `fetchUpcomingMovies success updates state to Success`() = runTest {
        val expectedMovies = listOf(
            Movie(id = 5, title = "Upcoming Movie 1", "",""),
            Movie(id = 6, title = "Upcoming Movie 2", "", ""))
        val movieResponse = MovieResponse(results = expectedMovies)
        coEvery { movieRepository.getUpcomingMovies() } returns ApiState.Success(movieResponse)
        viewModel.fetchUpcomingMovies()
        val state = viewModel.upcomingMovies.first()
        assertEquals(ApiState.Success(expectedMovies), state)
        coVerify { movieRepository.getUpcomingMovies() }
    }

    @Test
    fun `fetchUpcomingMovies failure updates state to Failure`() = runTest {
        val errorMessage = "Failed to fetch upcoming movies"
        coEvery { movieRepository.getUpcomingMovies() } returns ApiState.Failure(errorMessage)
        viewModel.fetchUpcomingMovies()
        val state = viewModel.upcomingMovies.first()
        assertEquals(ApiState.Failure(errorMessage), state)
        coVerify { movieRepository.getUpcomingMovies() }
    }

    @Test
    fun `fetchToRateMovies success updates state to Success ` () = runTest {
        val expectedMovies = listOf(
            Movie(id = 7, title = "Top Rated Movie 1", "", ""),
            Movie(id = 8, title = "Top Rated Movie 2", "", "")
        )
        val movieResponse = MovieResponse(results = expectedMovies)
        coEvery { movieRepository.getToRateMovies() } returns ApiState.Success(movieResponse)
        viewModel.fetchToRateMovies()
        val state = viewModel.rateMovies.first()
        assertEquals(ApiState.Success(expectedMovies), state)
        coVerify { movieRepository.getToRateMovies() }
    }

    @Test
    fun `fetchToRateMovies failure updates state to Failure`() = runTest {
        val errorMessage = "Failed to fetch top rated movies"
        coEvery { movieRepository.getToRateMovies() } returns ApiState.Failure(errorMessage)
        viewModel.fetchToRateMovies()
        val state = viewModel.rateMovies.first()
        assertEquals(ApiState.Failure(errorMessage), state)
        coVerify { movieRepository.getToRateMovies() }
    }

}



