package com.sarang.torang.di.main_di

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
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
    videoPlayer: @Composable (url: String, isPlaying: Boolean, onVideoClick: () -> Unit) -> Unit,
    onPressed: () -> Unit = {},
    onReleased: () -> Unit = {},
): @Composable ((
    feed: Feed,
    onLike: (Int) -> Unit,
    onFavorite: (Int) -> Unit,
    isLogin: Boolean,
    onVideoClick: () -> Unit,
    imageHeight: Int,
) -> Unit) =
    { feed, onLike, onFavirite, isLogin, onVideoClick, imageHeight ->
        Feed(
            review = feed.toReview(),
            isZooming = { Log.w("_provideFeed", "isZooming  is nothing") /*scrollEnabled = !it*/ },
            imageLoadCompose = provideTorangAsyncImage(),
            onComment = { onComment.invoke(feed.reviewId) },
            onShare = { if (isLogin) onShare.invoke(feed.reviewId) else rootNavController.emailLogin() },
            onMenu = { onMenu.invoke(feed.reviewId) },
            onName = { navController.navigate("profile/${feed.userId}") },
            onRestaurant = { rootNavController.restaurant(feed.restaurantId) },
            onImage = { rootNavController.imagePager(feed.reviewId, it) },
            onProfile = { navController.navigate("profile/${feed.userId}") },
            onLike = { if (isLogin) onLike.invoke(feed.reviewId) else rootNavController.emailLogin() },
            onFavorite = { if (isLogin) onFavirite.invoke(feed.reviewId) else rootNavController.emailLogin() },
            onLikes = { rootNavController.like(feed.reviewId) },
            expandableText = provideExpandableText(),
            isLogin = isLogin,
            videoPlayer = { videoPlayer.invoke(it, feed.isPlaying, onVideoClick) },
            imageHeight = if (imageHeight > 0) imageHeight.dp else 600.dp,
            onPressed = onPressed,
            onReleased = onReleased
        )
    }