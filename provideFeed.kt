package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.Feed
import com.sarang.torang.data.feed.Feed
import com.sarang.torang.di.feed_di.toReview
import com.sarang.torang.di.image.provideTorangAsyncImage

fun provideFeed(
    onComment: ((Int) -> Unit),
    onMenu: ((Int) -> Unit),
    onShare: ((Int) -> Unit),
    navController: NavHostController,
    rootNavController: RootNavController,
): @Composable (Feed, (Int) -> Unit, (Int) -> Unit) -> Unit = { feed, onLike, onFavirite ->
    Feed(
        review = feed.toReview(),
        isZooming = { /*scrollEnabled = !it*/ },
        progressTintColor = Color(0xFF000000),
        image = provideTorangAsyncImage(),
        onComment = { onComment.invoke(feed.reviewId) },
        onShare = { onShare.invoke(feed.reviewId) },
        onMenu = { onMenu.invoke(feed.reviewId) },
        onName = { navController.navigate("profile/${feed.userId}") },
        onRestaurant = { rootNavController.restaurant(feed.restaurantId) },
        onImage = { },
        onProfile = { navController.navigate("profile/${feed.userId}") },
        onLike = { onLike.invoke(feed.reviewId) },
        onFavorite = { onFavirite.invoke(feed.reviewId) }
    )
}