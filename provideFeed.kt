package com.sarang.torang.di.main_di

import androidx.navigation.NavHostController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.FeedItemClickEvents
import com.sarang.torang.compose.feed.type.FeedTypeData
import com.sarang.torang.viewmodel.FeedDialogsViewModel

fun generateCommonFeedItemClickEvent(
    feedData            : FeedTypeData,
    dialogsViewModel    : FeedDialogsViewModel,
    navController       : NavHostController,
    rootNavController   : RootNavController
) : FeedItemClickEvents{
    return FeedItemClickEvents(
        onComment           = { dialogsViewModel.onComment(feedData.feed.reviewId) },
        onMenu              = { dialogsViewModel.onMenu(feedData.feed.reviewId) },
        onShare             = { if (feedData.isLogin) dialogsViewModel.onShare(feedData.feed.reviewId) else rootNavController.emailLogin() },
        onName              = { navController.navigate("profile/${feedData.feed.userId}") },
        onProfile           = { navController.navigate("profile/${feedData.feed.userId}") },
        onRestaurant        = { rootNavController.restaurant(feedData.feed.restaurantId) },
        onImage             = { if (feedData.pageScrollable) rootNavController.imagePager(feedData.feed.reviewId, it) }, // TODO::[need optimize] 줌 상태에서 손을 때면 이미지 클릭 이벤트가 발생해 줌 하는동안은 pageScrollAble이 flase 상태여서 예외처리
        onLike              = { if (feedData.isLogin) feedData.onLike.invoke(feedData.feed.reviewId) else rootNavController.emailLogin() },
        onFavorite          = { if (feedData.isLogin) feedData.onFavorite.invoke(feedData.feed.reviewId) else rootNavController.emailLogin() },
        onLikes             = { rootNavController.like(feedData.feed.reviewId) })
}