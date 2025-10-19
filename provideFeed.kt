package com.sarang.torang.di.main_di

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.FeedItem
import com.sarang.torang.compose.feed.FeedItemClickEvents
import com.sarang.torang.di.feed_di.toReview
import com.sarang.torang.viewmodel.FeedDialogsViewModel
import com.sarang.torang.compose.feed.type.feedType
import com.sarang.torang.data.basefeed.FeedItemPageEvent

/**
 * @param onPage 페이지 콜백 Int: 현재 페이지, Boolean: 첫번째 페이지 여부, Boolean: 마지막 페이지 여부
 */
@Composable
fun provideFeed(
    tag                 : String                        = "__provideFeed",
    dialogsViewModel    : FeedDialogsViewModel,
    navController       : NavHostController,
    rootNavController   : RootNavController,
    onPage              : (FeedItemPageEvent) -> Unit   = { feedItemPageEvent -> Log.w(tag, "onPage callback is not set page: $feedItemPageEvent.page isFirst: $feedItemPageEvent.isFirst isLast: $feedItemPageEvent.isLast") }
): feedType =
    { feedData ->
        FeedItem(
            uiState             = feedData.feed.toReview(feedData.isLogin),
            feedItemClickEvents = FeedItemClickEvents(
            onComment           = { dialogsViewModel.onComment(feedData.feed.reviewId) },
            onShare             = { if (feedData.isLogin) dialogsViewModel.onShare(feedData.feed.reviewId) else rootNavController.emailLogin() },
            onMenu              = { dialogsViewModel.onMenu(feedData.feed.reviewId) },
            onName              = { navController.navigate("profile/${feedData.feed.userId}") },
            onRestaurant        = { rootNavController.restaurant(feedData.feed.restaurantId) },
            onImage             = { if (feedData.pageScrollable) rootNavController.imagePager(feedData.feed.reviewId, it) }, // TODO::[need optimize] 줌 상태에서 손을 때면 이미지 클릭 이벤트가 발생해 줌 하는동안은 pageScrollAble이 flase 상태여서 예외처리
            onProfile           = { navController.navigate("profile/${feedData.feed.userId}") },
            onLike              = { if (feedData.isLogin) feedData.onLike.invoke(feedData.feed.reviewId) else rootNavController.emailLogin() },
            onFavorite          = { if (feedData.isLogin) feedData.onFavorite.invoke(feedData.feed.reviewId) else rootNavController.emailLogin() },
            onLikes             = { rootNavController.like(feedData.feed.reviewId) }),
            onPage              = onPage,
            pageScrollAble      = feedData.pageScrollable
        )
    }