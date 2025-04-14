package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.Feed
import com.sarang.torang.data.feed.Feed
import com.sarang.torang.di.feed_di.toReview
import com.sarang.torang.di.image.provideTorangAsyncImage
import com.sarang.torang.di.video.provideVideoPlayer
import com.sarang.torang.viewmodels.FeedDialogsViewModel

/**
 * @param onPage 페이지 콜백 Int: 현재 페이지, Boolean: 첫번째 페이지 여부, Boolean: 마지막 페이지 여부
 */
@Composable
fun provideFeed(
    dialogsViewModel: FeedDialogsViewModel,
    navController: NavHostController,
    rootNavController: RootNavController,
    videoPlayer: @Composable (url: String, isPlaying: Boolean, onVideoClick: () -> Unit) -> Unit = provideVideoPlayer(),
    onPage: (Int, Boolean, Boolean) -> Unit = { _, _, _ -> }
): @Composable ((
    feed: Feed,
    onLike: (Int) -> Unit,
    onFavorite: (Int) -> Unit,
    isLogin: Boolean,
    onVideoClick: () -> Unit,
    imageHeight: Int,
) -> Unit) =
    { feed, onLike, onFavorite, isLogin, onVideoClick, imageHeight ->
        Feed(
            review = feed.toReview(),
            isZooming = { },
            imageLoadCompose = provideTorangAsyncImage(),
            onComment = { dialogsViewModel.onComment(feed.reviewId) },
            onShare = { if (isLogin) dialogsViewModel.onShare(feed.reviewId) else rootNavController.emailLogin() },
            onMenu = { dialogsViewModel.onMenu(feed.reviewId) },
            onName = { navController.navigate("profile/${feed.userId}") },
            onRestaurant = { rootNavController.restaurant(feed.restaurantId) },
            onImage = { rootNavController.imagePager(feed.reviewId, it) },
            onProfile = { navController.navigate("profile/${feed.userId}") },
            onLike = { if (isLogin) onLike.invoke(feed.reviewId) else rootNavController.emailLogin() },
            onFavorite = { if (isLogin) onFavorite.invoke(feed.reviewId) else rootNavController.emailLogin() },
            onLikes = { rootNavController.like(feed.reviewId) },
            expandableText = provideExpandableText(),
            isLogin = isLogin,
            videoPlayer = { videoPlayer.invoke(it, feed.isPlaying, onVideoClick) },
            imageHeight = if (imageHeight > 0) imageHeight.dp else 600.dp,
            onPage = onPage
        )
    }