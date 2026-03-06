package com.sarang.torang.di.main_di

import android.util.Log
import androidx.navigation.NavHostController
import com.sarang.torang.dialogsbox.compose.DialogsBoxViewModel
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.type.FeedTypeData
import com.sarang.torang.data.basefeed.FeedBottomEvents
import com.sarang.torang.data.basefeed.FeedItemClickEvents
import com.sarang.torang.data.basefeed.FeedTopEvents

fun generateCommonFeedItemClickEvent(
    feedData            : FeedTypeData,
    dialogsViewModel    : DialogsBoxViewModel,
    navController       : NavHostController,
    rootNavController   : RootNavController,
    onError             : (String) -> Unit = {}
) : FeedItemClickEvents {
    val tag = "__generateCommonFeedItemClickEvent"
    return FeedItemClickEvents(
        feedTopEvents = FeedTopEvents(
            onMenu              = { dialogsViewModel.onMenu(feedData.feed.reviewId) },
            onName              = { rootNavController.profile(feedData.feed.userId) },
            onProfile           = { rootNavController.profile(feedData.feed.userId) },
            onRestaurant        = { rootNavController.restaurant(feedData.feed.restaurantId) },
        ),
        feedBottomEvents = FeedBottomEvents(
            onComment           = { dialogsViewModel.onComment(feedData.feed.reviewId) },
            onShare             = { if (feedData.isLogin) {
                try {
                    dialogsViewModel.onShare(feedData.feed.reviewId)
                }catch (e : Exception){
                    Log.e(tag, e.toString())
                    onError.invoke(e.message.toString())
                }
            }
            else rootNavController.emailLogin() },
            onLike              = { if (feedData.isLogin) feedData.onLike.invoke(feedData.feed.reviewId)
            else { Log.i(tag, "로그인을 해주세요.")
                rootNavController.emailLogin() } },
            onFavorite          = { if (feedData.isLogin) feedData.onFavorite.invoke(feedData.feed.reviewId)
            else { Log.i(tag, "로그인을 해주세요.")
                rootNavController.emailLogin() } },
        ),
        onImage             = { if (feedData.pageScrollable) rootNavController.imagePager(feedData.feed.reviewId, it) }, // TODO::[need optimize] 줌 상태에서 손을 때면 이미지 클릭 이벤트가 발생해 줌 하는동안은 pageScrollAble이 flase 상태여서 예외처리
        onLikes             = { rootNavController.like(feedData.feed.reviewId) })
}