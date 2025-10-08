package com.sarang.torang.di.main_di

import android.util.Log
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.FeedScreenInMain
import com.sarang.torang.compose.feed.internal.components.LocalExpandableTextType
import com.sarang.torang.compose.feed.state.FeedScreenState
import com.sarang.torang.compose.feed.state.rememberFeedScreenState
import com.sarang.torang.compose.feed.type.LocalBottomDetectingLazyColumnType
import com.sarang.torang.compose.feed.type.LocalFeedCompose
import com.sarang.torang.compose.feed.type.LocalPullToRefreshLayoutType
import com.sarang.torang.compose.feed.type.feedType
import com.sarang.torang.data.basefeed.FeedItemPageEvent
import com.sarang.torang.di.basefeed_di.CustomExpandableTextType
import com.sarang.torang.di.feed_di.CustomBottomDetectingLazyColumnType
import com.sarang.torang.di.feed_di.customPullToRefresh
import com.sarang.torang.viewmodel.FeedDialogsViewModel

/**
 * 피드 화면과 프로필 화면
 * @param onAlarm 상단바에서 알림 클릭 콜백
 * @param onPage 페이지 콜백 Int: 현재 페이지, Boolean: 첫번째 페이지 여부, Boolean: 마지막 페이지 여부
 * @param swipeAble 좌우 스와이프 가능 여부
 */
@Composable
fun FeedScreenWithProfile(
    tag : String = "__FeedScreenWithProfile",
    rootNavController: RootNavController,
    feedNavController: NavHostController,
    dialogsViewModel: FeedDialogsViewModel,
    feedScreenState : FeedScreenState   = rememberFeedScreenState(),
    onChat: () -> Unit,
    onMessage: (Int) -> Unit,
    onAlarm: () -> Unit = { Log.w("__FeedScreenWithProfile", "onAlarm is not implemented") },
    onPage              : (FeedItemPageEvent) -> Unit   = { feedItemPageEvent -> Log.w(tag, "onPage callback is not set page: $feedItemPageEvent.page isFirst: $feedItemPageEvent.isFirst isLast: $feedItemPageEvent.isLast") },
    scrollEnabled: Boolean = true,
    swipeAble : Boolean = true
) {
    NavHost(navController = feedNavController, startDestination = "feed") {
        composable("feed") {
            CompositionLocalProvider(
                LocalFeedCompose provides MainFeed(
                    dialogsViewModel = dialogsViewModel,
                    feedNavController = feedNavController,
                    rootNavController = rootNavController,
                    onPage = onPage),
                LocalBottomDetectingLazyColumnType provides CustomBottomDetectingLazyColumnType,
                LocalPullToRefreshLayoutType provides customPullToRefresh,
                //LocalFeedImageLoader provides CustomFeedImageLoader, // 상위 zoom 이미지 로더로 설정
                LocalExpandableTextType provides CustomExpandableTextType
            ){
                FeedScreenInMain(
                    onAddReview = onChat,
                    feedScreenState = feedScreenState,
                    onAlarm = onAlarm,
                    scrollEnabled = scrollEnabled,
                    pageScrollable = swipeAble,
                    contentWindowInsets = WindowInsets(0.dp)
                )
            }
        }
        composable("profile/{id}"){
            ProvideProfileScreen(
                rootNavController = rootNavController,
                navController = feedNavController,
                onMessage = onMessage,
                navBackStackEntry = it)
        }
    }
}

fun MainFeed(
    tag : String = "__MainFeed",
    dialogsViewModel: FeedDialogsViewModel,
    feedNavController: NavHostController,
    rootNavController: RootNavController,
    onPage              : (FeedItemPageEvent) -> Unit   = { feedItemPageEvent -> Log.w(tag, "onPage callback is not set page: $feedItemPageEvent.page isFirst: $feedItemPageEvent.isFirst isLast: $feedItemPageEvent.isLast") }
): feedType = { data ->
    provideFeed(
        dialogsViewModel = dialogsViewModel,
        navController = feedNavController,
        rootNavController = rootNavController,
        onPage = onPage
    ).invoke(data)
}