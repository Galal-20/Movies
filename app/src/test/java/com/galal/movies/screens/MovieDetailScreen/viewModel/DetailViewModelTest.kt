package com.galal.movies.screens.MovieDetailScreen.viewModel


import com.galal.movies.data.api.ApiState
import com.galal.movies.data.local.FavoriteMovieEntity
import com.galal.movies.data.repository.MovieRepository
import com.galal.movies.model.Cast
import com.galal.movies.model.Movie
import com.galal.movies.model.MovieDetail
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
class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel
    private val movieRepository: MovieRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DetailViewModel(movieRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchMovieDetails success updates state to Success`() = runTest {
        val movieId = 1
        val expectedMovieDetail = MovieDetail(
            id = movieId, title = "Movie Detail", overview = "Movie overview",
            genres = emptyList(),runtime = 120, release_date = "2023-09-11",
            spoken_languages = emptyList()
            )
        coEvery { movieRepository.getMovieDetails(movieId) } returns ApiState.Success(expectedMovieDetail)
        viewModel.fetchMovieDetails(movieId)
        val state = viewModel.movieDetails.first()
        assertEquals(ApiState.Success(expectedMovieDetail), state)
        coVerify { movieRepository.getMovieDetails(movieId) }
    }

    @Test
    fun `fetchMovieDetails failure updates state to Failure`() = runTest {
        val movieId = 1
        val errorMessage = "Error fetching movie details"
        coEvery { movieRepository.getMovieDetails(movieId) } returns ApiState.Failure(errorMessage)
        viewModel.fetchMovieDetails(movieId)
        val state = viewModel.movieDetails.first()
        assertEquals(ApiState.Failure(errorMessage), state)
        coVerify { movieRepository.getMovieDetails(movieId) }
    }

    @Test
    fun `fetchMovieCast success updates state to Success`() = runTest {
        val movieId = 1
        val expectedCast = listOf(
            Cast(id = 1, name = "Actor 1", profile_path = "profile1.jpg"),
            Cast(id = 2, name = "Actor 2", profile_path = "profile2.jpg"))
        coEvery { movieRepository.getMovieCredits(movieId) } returns ApiState.Success(expectedCast)
        viewModel.fetchMovieCast(movieId)
        val state = viewModel.movieCast.first()
        assertEquals(ApiState.Success(expectedCast), state)
        coVerify { movieRepository.getMovieCredits(movieId) }
    }

    @Test
    fun `fetchMovieCast failure updates state to Failure`() = runTest {
        val movieId = 1
        val errorMessage = "Error fetching movie cast"
        coEvery { movieRepository.getMovieCredits(movieId) } returns ApiState.Failure(errorMessage)
        viewModel.fetchMovieCast(movieId)
        val state = viewModel.movieCast.first()
        assertEquals(ApiState.Failure(errorMessage), state)
        coVerify { movieRepository.getMovieCredits(movieId) }
    }

    @Test
    fun `fetchSimilarMovies success updates state to Success`() = runTest {
        val movieId = "1"
        val expectedMovies = listOf(
            Movie(id = 3, title = "Similar Movie 1", release_date = "2023-09-11", poster_path = "poster3.jpg"),
            Movie(id = 4, title = "Similar Movie 2", release_date = "2023-09-12", poster_path = "poster4.jpg"))
        val movieResponse = MovieResponse(results = expectedMovies)
        coEvery { movieRepository.getSimilarMovies(movieId) } returns ApiState.Success(movieResponse)
        viewModel.fetchSimilarMovies(movieId)
        val state = viewModel.similarMovies.first()
        assertEquals(ApiState.Success(expectedMovies), state)
        coVerify { movieRepository.getSimilarMovies(movieId) }
    }

    @Test
    fun `fetchSimilarMovies failure updates state to Failure`() = runTest {
        val movieId = "1"
        val errorMessage = "Failed to fetch similar movies"
        coEvery { movieRepository.getSimilarMovies(movieId) } returns ApiState.Failure(errorMessage)
        viewModel.fetchSimilarMovies(movieId)
        val state = viewModel.similarMovies.first()
        assertEquals(ApiState.Failure(errorMessage), state)
        coVerify { movieRepository.getSimilarMovies(movieId) }
    }

    @Test
    fun `fetchMovieVideos success updates state to Success`() = runTest {
        val movieId = 1
        val expectedVideos = listOf(
            Movie(id = 3, title = "Similar Movie 1", release_date = "2023-09-11", poster_path = "poster3.jpg"),
            Movie(id = 4, title = "Similar Movie 2", release_date = "2023-09-12", poster_path = "poster4.jpg")
        )
        val movieResponse = MovieResponse(results = expectedVideos)
        coEvery { movieRepository.getSimilarMovies(movieId.toString()) } returns ApiState.Success(movieResponse)
        viewModel.fetchSimilarMovies(movieId.toString())
        val state = viewModel.similarMovies.first()
        assertEquals(ApiState.Success(expectedVideos), state)
        coVerify { movieRepository.getSimilarMovies(movieId.toString()) }
    }

    @Test
    fun `fetchMovieVideos failure updates state to Failure`() = runTest {
        val movieId = 1
        val errorMessage = "Failed to fetch video movies"
        coEvery { movieRepository.getSimilarMovies(movieId.toString()) } returns ApiState.Failure(errorMessage)
        viewModel.fetchSimilarMovies(movieId.toString())
        val state = viewModel.similarMovies.first()
        assertEquals(ApiState.Failure(errorMessage), state)
        coVerify { movieRepository.getSimilarMovies(movieId.toString()) }
    }

    @Test
    fun `addToFavorite calls repository with correct FavoriteMovieEntity`() = runTest {
        // Arrange
        val movieDetail = MovieDetail(
            id = 1,
            title = "Test Movie",
            overview = "Test overview",
            genres = listOf(),  // Assuming genres is a list of objects with 'name' field
            runtime = 120,
            release_date = "2023-09-11",
            spoken_languages = listOf(),
            poster_path = "testPosterPath.jpg",
            vote_average = 8.5,
            backdrop_path = "testBackdropPath.jpg",
            tagline = "Test tagline"
        )

        coEvery { movieRepository.addFavoriteMovie(any()) } returns Unit

        // Act
        viewModel.addToFavorite(movieDetail)

        // Assert
        coVerify {
            movieRepository.addFavoriteMovie(
                FavoriteMovieEntity(
                    id = movieDetail.id,
                    title = movieDetail.title,
                    posterPath = movieDetail.poster_path ?: "",
                    releaseDate = movieDetail.release_date,
                    voteAverage = movieDetail.vote_average!!,
                    overview = movieDetail.overview,
                    runtime = movieDetail.runtime,
                    backdropPath = movieDetail.backdrop_path ?: "",
                    tagline = movieDetail.tagline ?: "",
                    genres = movieDetail.genres.joinToString { it.name },
                    spokenLanguages = movieDetail.spoken_languages.joinToString { it.name }
                )
            )
        }
    }
}

