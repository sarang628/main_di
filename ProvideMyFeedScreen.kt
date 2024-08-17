package com.sarang.torang.di.main_di

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
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
        rootNavController = rootNavController
    ) {
        MyFeedScreen(
            reviewId = navBackStackEntry.arguments?.getString("reviewId")?.toInt()
                ?: 0,
            listState = rememberLazyListState(),
            feed = { feed, onLike, onFavorite ->
                Feed(
                    review = feed.toReview(),
                    isZooming = { /*scrollEnabled = !it*/ },
                    imageLoadCompose = provideTorangAsyncImage(),
                    onComment = { dialogsViewModel.onComment(feed.reviewId) },
                    onShare = { dialogsViewModel.onShare(feed.reviewId) },
                    onMenu = { dialogsViewModel.onMenu(feed.reviewId) },
                    onName = { navController.navigate("profile/${feed.userId}") },
                    onRestaurant = { rootNavController.restaurant(feed.restaurantId) },
                    onImage = { rootNavController.imagePager(feed.reviewId, 0) },
                    onProfile = { navController.navigate("profile/${feed.userId}") },
                    onLike = { onLike.invoke(feed.reviewId) },
                    onFavorite = { onFavorite.invoke(feed.reviewId) },
                    onLikes = { rootNavController.like(feed.reviewId) }
                )
            },
            onBack = { navController.popBackStack() }
        )
    }
}