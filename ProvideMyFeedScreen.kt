package com.sarang.torang.di.main_di

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.Feed
import com.sarang.torang.compose.feed.MyFeedScreen
import com.sarang.torang.di.feed_di.toReview
import com.sarang.torang.di.image.provideTorangAsyncImage
import com.sarang.torang.viewmodels.FeedDialogsViewModel

@Composable
fun ProvideMyFeedScreen(
    dialogsViewModel: FeedDialogsViewModel = hiltViewModel(),
    navController: NavHostController,
    rootNavController: RootNavController,
    navBackStackEntry: NavBackStackEntry,
) {
    ProvideMainDialog(
        dialogsViewModel = dialogsViewModel,
        navController = rootNavController
    ) {
        MyFeedScreen(
            reviewId = navBackStackEntry.arguments?.getString("reviewId")?.toInt()
                ?: 0,
            listState = rememberLazyListState(),
            feed = { it, onLike, onFavorite ->
                Feed(
                    review = it.toReview(),
                    isZooming = { /*scrollEnabled = !it*/ },
                    progressTintColor = Color(0xFF000000),
                    image = provideTorangAsyncImage(),
                    onComment = { dialogsViewModel.onComment(it.reviewId) },
                    onShare = { dialogsViewModel.onShare(it.reviewId) },
                    onMenu = { dialogsViewModel.onMenu(it.reviewId) },
                    onName = { navController.navigate("profile/${it.userId}") },
                    onRestaurant = { rootNavController.restaurant(it.restaurantId) },
                    onImage = { rootNavController.imagePager(it, 0) },
                    onProfile = { navController.navigate("profile/${it.userId}") },
                    onLike = { onLike.invoke(it.reviewId) },
                    onFavorite = { onFavorite.invoke(it.reviewId) }
                )
            },
            onBack = { navController.popBackStack() }
        )
    }
}