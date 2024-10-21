package com.galal.movies


import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import com.galal.movies.model.Movie
import com.galal.movies.screens.Search.view.MovieList
import com.galal.movies.utils.NoInternetConnection
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun searchScreen_displaysMovieResults() {
        val mockMovies = listOf(
            Movie(id = 1, title = "Inception", poster_path = "/inception.jpg", release_date = "2010"),
            Movie(id = 2, title = "Avatar", poster_path = "/avatar.jpg", release_date = "2009")
        )

        composeTestRule.setContent {
            MovieList(movies = mockMovies, navController = NavHostController(composeTestRule.activity))
        }

        composeTestRule.onNodeWithText("Inception").assertIsDisplayed()
        composeTestRule.onNodeWithText("Avatar").assertIsDisplayed()
    }

    @Test
    fun searchScreen_showsNoInternetMessage() {
        composeTestRule.setContent {
            NoInternetConnection()
        }

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.no_internet_connection))
            .assertIsDisplayed()
    }
}
