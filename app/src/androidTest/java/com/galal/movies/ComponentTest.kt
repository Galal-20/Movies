package com.galal.movies

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.galal.movies.model.Cast
import com.galal.movies.model.Movie
import com.galal.movies.screens.MovieDetailScreen.view.CastMemberItem
import com.galal.movies.screens.MovieDetailScreen.view.Chip
import com.galal.movies.screens.MovieDetailScreen.view.SimilarMovieItem
import com.galal.movies.screens.MovieListScreen.view.MovieItem
import com.galal.movies.utils.AppHeader
import com.galal.movies.utils.LoadingIndicator
import com.galal.movies.utils.NoInternetConnection
import com.galal.movies.utils.SliderWithIndicator
import org.junit.Rule
import org.junit.Test

class ComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun appHeader_displaysTitleAndHandlesSearchClick() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val testTitle = "Home"

        composeTestRule.setContent {
            AppHeader(navController = navController, title = testTitle)
        }

        composeTestRule.onNodeWithText(testTitle).assertIsDisplayed()
    }


    @Test
    fun loadingIndicator_displaysCircularProgressIndicator() {
        composeTestRule.setContent {
            LoadingIndicator()
        }

        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertIsDisplayed()
    }

    @Test
    fun noInternetConnection_displaysLottieAndMessage() {
        composeTestRule.setContent {
            NoInternetConnection()
        }

        composeTestRule.onNodeWithText("No Internet Connection").assertIsDisplayed()
    }

    @Test
    fun sliderWithIndicator_displaysMoviesAndHandlesClicks() {
        val movies = listOf(
            Movie(id = 1, title = "Movie 1","11-10-2026", poster_path = R.drawable.logo.toString()),
            Movie(id = 2, title = "Movie 2","10-10-2025", poster_path = R.drawable.logo.toString()),
            Movie(id = 3, title = "Movie 3","11-10-2024", poster_path = R.drawable.logo.toString())
        )

        var clickedMovieId: Int? = null
        val onMovieClick: (Int) -> Unit = { clickedMovieId = it }

        composeTestRule.setContent {
            SliderWithIndicator(movies = movies, onMovieClick = onMovieClick)
        }
        composeTestRule.onNodeWithText(movies[0].title).performClick()

        assert(clickedMovieId == movies[0].id)

    }

    @Test
    fun movieItem_displaysMovieDetailsAndHandlesClick() {
        val movie = Movie(
            id = 1,
            title = "Venom",
            poster_path = R.drawable.logo.toString(),
            release_date = "2024-10-20"
        )

        var clickedMovieId: Int? = null
        val onMovieClick: (Int) -> Unit = { clickedMovieId = it }

        composeTestRule.setContent {
            MovieItem(movie = movie, onClick = onMovieClick)
        }

        composeTestRule.onNodeWithText(movie.title).assertIsDisplayed()

        composeTestRule.onNodeWithText("Release: ${movie.release_date}").assertIsDisplayed()

        composeTestRule.onNodeWithText(movie.title).performClick()

        assert(clickedMovieId == movie.id)

        composeTestRule.onNodeWithText(movie.title).assertIsDisplayed()
    }

    @Test
    fun chip_displaysCorrectText() {
        val chipText = "Action"

        composeTestRule.setContent {
            Chip(text = chipText)
        }
        composeTestRule.onNodeWithText(chipText).assertIsDisplayed()
    }


    @Test
    fun castMemberItem_displaysPlaceholder_whenProfilePathIsNull() {
        val cast = Cast(
            id = 1,
            name = "Jane Smith",
            profile_path = null
        )

        composeTestRule.setContent {
            CastMemberItem(cast = cast)
        }

        composeTestRule.onNodeWithText(cast.name).assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Cast Member Placeholder").assertExists()
    }

}
