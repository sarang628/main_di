package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.FeedItem
import com.sarang.torang.compose.feed.feedType
import com.sarang.torang.di.feed_di.toReview
import com.sarang.torang.viewmodels.FeedDialogsViewModel

/**
 * @param onPage 페이지 콜백 Int: 현재 페이지, Boolean: 첫번째 페이지 여부, Boolean: 마지막 페이지 여부
 */
@Composable
fun provideFeed(
    dialogsViewModel: FeedDialogsViewModel,
    navController: NavHostController,
    rootNavController: RootNavController,
    onPage: (Int, Boolean, Boolean) -> Unit = { _, _, _ -> },
): feedType =
    { feed, onLike, onFavorite, isLogin, onVideoClick, imageHeight, pageScrollAble ->
        FeedItem(
            uiState = feed.toReview(),
            isZooming = { },
            onComment = { dialogsViewModel.onComment(feed.reviewId) },
            onShare = { if (isLogin) dialogsViewModel.onShare(feed.reviewId) else rootNavController.emailLogin() },
            onMenu = { dialogsViewModel.onMenu(feed.reviewId) },
            onName = { navController.navigate("profile/${feed.userId}") },
            onRestaurant = { rootNavController.restaurant(feed.restaurantId) },
            onImage = { if (pageScrollAble) rootNavController.imagePager(feed.reviewId, it) }, // TODO::[need optimize] 줌 상태에서 손을 때면 이미지 클릭 이벤트가 발생해 줌 하는동안은 pageScrollAble이 flase 상태여서 예외처리
            onProfile = { navController.navigate("profile/${feed.userId}") },
            onLike = { if (isLogin) onLike.invoke(feed.reviewId) else rootNavController.emailLogin() },
            onFavorite = { if (isLogin) onFavorite.invoke(feed.reviewId) else rootNavController.emailLogin() },
            onLikes = { rootNavController.like(feed.reviewId) },
            isLogin = isLogin,
            onPage = onPage,
            pageScrollAble = pageScrollAble
        )
    }