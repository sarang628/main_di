package com.sarang.torang.di.main_di

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.Feed
import com.sarang.torang.data.feed.Feed
import com.sarang.torang.di.feed_di.toReview
import com.sarang.torang.di.image.provideTorangAsyncImage
import com.sarang.torang.di.torang.provideExpandableText
import com.sryang.library.ExpandableText

fun provideFeed(
    onComment: ((Int) -> Unit),
    onMenu: ((Int) -> Unit),
    onShare: ((Int) -> Unit),
    navController: NavHostController,
    rootNavController: RootNavController,
): @Composable (Feed, (Int) -> Unit, (Int) -> Unit) -> Unit = { feed, onLike, onFavirite ->
    Feed(
        review = feed.toReview(),
        isZooming = { Log.w("_provideFeed", "isZooming  is nothing") /*scrollEnabled = !it*/ },
        imageLoadCompose = provideTorangAsyncImage(),
        onComment = { onComment.invoke(feed.reviewId) },
        onShare = { onShare.invoke(feed.reviewId) },
        onMenu = { onMenu.invoke(feed.reviewId) },
        onName = { navController.navigate("profile/${feed.userId}") },
        onRestaurant = { rootNavController.restaurant(feed.restaurantId) },
        onImage = { rootNavController.imagePager(feed.reviewId, it) },
        onProfile = { navController.navigate("profile/${feed.userId}") },
        onLike = { onLike.invoke(feed.reviewId) },
        onFavorite = { onFavirite.invoke(feed.reviewId) },
        onLikes = { rootNavController.like(feed.reviewId) },
        expandableText = provideExpandableText()
    )
}