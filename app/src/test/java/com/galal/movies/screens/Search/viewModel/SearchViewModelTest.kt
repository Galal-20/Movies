package com.galal.movies.screens.Search.viewModel


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
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private val movieRepository: MovieRepository = mockk()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel(movieRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllMovies success updates state to Success`() = runTest {
        val nowPlayingMovies = listOf(
            Movie(id = 1, title = "Now Playing Movie", release_date = "2023-09-11", poster_path = "poster1.jpg"))
        val popularMovies = listOf(
            Movie(id = 2, title = "Popular Movie", release_date = "2023-09-12", poster_path = "poster2.jpg"))
        val upcomingMovies = listOf(
            Movie(id = 3, title = "Upcoming Movie", release_date = "2023-09-13", poster_path = "poster3.jpg"))

        val nowPlayingResponse = MovieResponse(results = nowPlayingMovies)
        val popularResponse = MovieResponse(results = popularMovies)
        val upcomingResponse = MovieResponse(results = upcomingMovies)
        coEvery { movieRepository.getNowPlayingMovies() } returns ApiState.Success(nowPlayingResponse)
        coEvery { movieRepository.getPopularMovies() } returns ApiState.Success(popularResponse)
        coEvery { movieRepository.getUpcomingMovies() } returns ApiState.Success(upcomingResponse)
        viewModel.getAllMovies()
        val expectedMovies = nowPlayingMovies + popularMovies + upcomingMovies
        val state = viewModel.searchResults.first()
        assertEquals(ApiState.Success(expectedMovies), state)
        coVerify { movieRepository.getNowPlayingMovies() }
        coVerify { movieRepository.getPopularMovies() }
        coVerify { movieRepository.getUpcomingMovies() }
    }

    @Test
    fun `getAllMovies failure updates state to Failure`() = runTest {
        coEvery { movieRepository.getNowPlayingMovies() } returns ApiState.Failure("Error loading now playing")
        coEvery { movieRepository.getPopularMovies() } returns ApiState.Success(MovieResponse(results = emptyList()))
        coEvery { movieRepository.getUpcomingMovies() } returns ApiState.Success(MovieResponse(results = emptyList()))
        viewModel.getAllMovies()
        val state = viewModel.searchResults.first()
        assertEquals(ApiState.Failure("Failed to load all movies"), state)
        coVerify { movieRepository.getNowPlayingMovies() }
        coVerify { movieRepository.getPopularMovies() }
        coVerify { movieRepository.getUpcomingMovies() }
    }

    @Test
    fun `searchMovies success updates state to Success`() = runTest {
        val query = "Avengers"
        val searchMovies = listOf(
            Movie(id = 1, title = "Avengers: Endgame", release_date = "2023-09-11", poster_path = "poster1.jpg"))
        val searchResponse = MovieResponse(results = searchMovies)
        coEvery { movieRepository.searchMovies(query) } returns ApiState.Success(searchResponse)
        viewModel.searchMovies(query)
        val state = viewModel.searchResults.first()
        assertEquals(ApiState.Success(searchMovies), state)
        coVerify { movieRepository.searchMovies(query) }
    }

    @Test
    fun `searchMovies failure updates state to Failure`() = runTest {
        val query = "Avengers"
        val errorMessage = "Error searching movies"
        coEvery { movieRepository.searchMovies(query) } returns ApiState.Failure(errorMessage)
        viewModel.searchMovies(query)
        val state = viewModel.searchResults.first()
        assertEquals(ApiState.Failure(errorMessage), state)
        coVerify { movieRepository.searchMovies(query) }
    }

}

