package com.galal.movies.screens.FavouriteScreen.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.galal.movies.R
import com.galal.movies.screens.FavouriteScreen.viewModel.FavouriteViewModel
import com.galal.movies.utils.AppBarHeader
import kotlinx.coroutines.launch

@Composable
fun FavouriteScreen(navController: NavHostController, viewModel: FavouriteViewModel) {

    val favoriteMovies by viewModel.favoriteMovies.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.getFavoriteMovies()
    }

    LazyColumn(
        Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        // AppBar Header
        item {
            AppBarHeader(navController, stringResource(R.string.favourite))
        }

        // Display each favorite movie
        items(favoriteMovies.size) { index ->
            val posterPath = favoriteMovies[index].posterPath
            val fullImageUrl = if (posterPath?.startsWith('/') == true) {
                "https://image.tmdb.org/t/p/w500$posterPath"
            } else {
                 posterPath
            }

            CardItem(
                title = favoriteMovies[index].title,
                rate = stringResource(R.string.rating)+": "+
                        String.format("%.1f", favoriteMovies[index].voteAverage),
                imageUrl = fullImageUrl,
                onDeleteClick = { viewModel.deleteMovie(favoriteMovies[index].id)},
                onItemClick = {navController.navigate("movie_detail/{movieId}".replace("{movieId}", favoriteMovies[index].id.toString()))}
            )
        }
    }
}




/*
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardItem(
    imageUrl: String,
    title: String,
    rate: String,
    onDeleteClick: () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Dismiss state for swipe gesture
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToStart) {
                showDialog.value = true
            }
            false
        }
    )

    // Animate the width of the background based on swipe progress
    val swipeProgress = animateFloatAsState(
        targetValue = if (dismissState.offset.value < 0) -dismissState.offset.value else 0f
    ).value

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(swipeProgress.dp)  // Adjust width based on swipe progress
                    .background(Color.Red)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Delete",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Icon",
                        tint = Color.White
                    )
                }
            }
        },
        dismissContent = {
            // Card Content
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardColors(Color.White, Color.White, Color.White, Color.White),
                border = BorderStroke(color = Color.Black, width = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Image
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Titles
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )
                        Text(
                            text = rate,
                            style = MaterialTheme.typography.body2,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    )

    // Alert Dialog
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Delete Item") },
            text = { Text(text = "Are you sure you want to delete this item?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick()
                        showDialog.value = false
                        coroutineScope.launch {
                            dismissState.reset()
                        }
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        coroutineScope.launch {
                            dismissState.reset()
                        }
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}*/

/*
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardItem(
    imageUrl: String,
    title: String,
    rate: String,
    onDeleteClick: () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Dismiss state for swipe gesture
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToStart) {
                showDialog.value = true
            }
            false
        }
    )

    // Animate the width of the background based on swipe progress
    val swipeProgress = animateFloatAsState(
        targetValue = if (dismissState.offset.value < 0) -dismissState.offset.value else 0f
    ).value

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()  // Fills the whole width
                    .background(Color.Transparent)  // Transparent base
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)  // Aligns background content to the end
                        .width(swipeProgress.dp)  // Dynamic width based on swipe progress
                        .fillMaxHeight()
                        .background(Color.Red)
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Delete",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Icon",
                            tint = Color.White
                        )
                    }
                }
            }
        },
        dismissContent = {
            // Card Content
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardColors(Color.White, Color.White, Color.White, Color.White),
                border = BorderStroke(color = Color.Black, width = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Image
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Titles
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )
                        Text(
                            text = rate,
                            style = MaterialTheme.typography.body2,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    )

    // Alert Dialog
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Delete Item") },
            text = { Text(text = "Are you sure you want to delete this item?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick()
                        showDialog.value = false
                        coroutineScope.launch {
                            dismissState.reset()
                        }
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        coroutineScope.launch {
                            dismissState.reset()
                        }
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}
*/
/*
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardItem(
    imageUrl: String,
    title: String,
    rate: String,
    onDeleteClick: () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Dismiss state for swipe gesture
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToStart) {
                showDialog.value = true
            }
            false
        }
    )

    // Animate the width of the background based on swipe progress
    val swipeProgress = animateFloatAsState(
        targetValue = if (dismissState.offset.value < 0) -dismissState.offset.value else 0f
    ).value

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()  // Makes the background fill the entire card width
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .width(swipeProgress.dp)  // Width grows with swipe progress
                        .fillMaxHeight()
                        .background(Color.Red)
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Delete",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Icon",
                            tint = Color.White
                        )
                    }
                }
            }
        },
        dismissContent = {
            // Card Content
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardColors(Color.White, Color.White, Color.White, Color.White),
                border = BorderStroke(color = Color.Black, width = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Image
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Titles
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )
                        Text(
                            text = rate,
                            style = MaterialTheme.typography.body2,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    )

    // Alert Dialog
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Delete Item") },
            text = { Text(text = "Are you sure you want to delete this item?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick()
                        showDialog.value = false
                        coroutineScope.launch {
                            dismissState.reset()
                        }
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        coroutineScope.launch {
                            dismissState.reset()
                        }
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}*/

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardItem(
    imageUrl: String,
    title: String,
    rate: String,
    onDeleteClick: () -> Unit,
    onItemClick: () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Dismiss state for swipe gesture
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToStart) {
                showDialog.value = true
            }
            false
        }
    )

    // Animate the width of the background based on swipe progress
    val swipeProgress = animateFloatAsState(
        targetValue = if (dismissState.offset.value < 0) -dismissState.offset.value else 0f
    ).value

    // Outer Box to add padding around the SwipeToDismiss
    Box(
        modifier = Modifier
            .padding(
                start = 15.dp,
                end = 15.dp,
                top = 10.dp,
                bottom = 10.dp
            )  // Add padding outside of the SwipeToDismiss to keep it constant
            .clip(RoundedCornerShape(14.dp))
    ) {
        SwipeToDismiss(
            state = dismissState,
            directions = setOf(DismissDirection.EndToStart),
            background = {
                // Background fills and grows from right to left on swipe
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()  // Fill the entire card width without padding
                        .background(Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .width(swipeProgress.dp)  // Expanding width based on swipe progress
                            .fillMaxHeight()
                            .background(Color.Red)
                            .padding(8.dp)  // Padding inside the swipe background area
                    ) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.delete),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete_icon),
                                tint = Color.White
                            )
                        }
                    }
                }
            },
            dismissContent = {
                // Card Content without padding
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardColors(Color.White, Color.White, Color.White, Color.White),
                    border = BorderStroke(color = Color.Black, width = 3.dp),
                    onClick = onItemClick
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)  // Padding inside the card itself
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Image
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // Titles
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                                color = Color.Black
                            )
                            Text(
                                text = rate,
                                style = MaterialTheme.typography.body2,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        )
    }

    // Alert Dialog
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = stringResource(R.string.delete_item)) },
            text = { Text(
                text = stringResource(R.string.are_you_sure_you_want_to_delete, title)
            ) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick()
                        showDialog.value = false
                        coroutineScope.launch {
                            dismissState.reset()
                        }
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        coroutineScope.launch {
                            dismissState.reset()
                        }
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}
