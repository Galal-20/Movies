package com.galal.movies.screens.Splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.galal.movies.R
import com.galal.movies.utils.ReusableLottie
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navHostController: NavHostController){
    var showSplash by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(3000)
        showSplash = false
        navHostController.navigate("movie_list") {
            popUpTo("splash_screen") { inclusive = true }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        ReusableLottie(R.raw.splash, null, size = 400.dp, 1f)
    }
}