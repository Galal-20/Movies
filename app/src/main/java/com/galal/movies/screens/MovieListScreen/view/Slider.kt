@file:Suppress("DEPRECATION")

package com.galal.movies.screens.MovieListScreen.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.galal.movies.model.Movie
import com.galal.movies.utils.Constants
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SliderWithIndicator(movies: List<Movie>, slideDuration: Long = 3000L) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    // Automatically scrolls through pages with a delay
    LaunchedEffect(pagerState) {
        coroutineScope.launch {
            while (true) {
                delay(slideDuration)
                val nextPage = (pagerState.currentPage + 1) % movies.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Horizontal pager to display the movies
        HorizontalPager(
            count = movies.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            val movie = movies[page]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(color = Color(0xFFEFEEEE), shape = RoundedCornerShape(28.dp))
            ) {
                Image(
                    painter = rememberImagePainter(data = "${Constants.BASE_POSTER_IMAGE_URL}${movie.poster_path}"),
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                        .clip(RoundedCornerShape(28.dp)),
                    contentScale = ContentScale.Crop
                )

                // Displaying the movie title
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        //.background(Color.Black.copy(alpha = 0.6f))
                        .padding(8.dp)
                ) {
                    Text(
                        text = movie.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }

       /* HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            activeColor = Color.Black,
            inactiveColor = Color.Gray,
            indicatorWidth = 12.dp,
            spacing = 8.dp
        )*/
    }
}
