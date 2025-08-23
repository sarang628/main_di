package com.sarang.torang.di.main_di

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.FeedScreenInMain
import com.sarang.torang.compose.feed.LocalPullToRefreshLayoutType
import com.sarang.torang.compose.feed.component.LocalBottomDetectingLazyColumnType
import com.sarang.torang.compose.feed.component.LocalFeedCompose
import com.sarang.torang.compose.feed.feedType
import com.sarang.torang.compose.feed.internal.components.LocalExpandableTextType
import com.sarang.torang.compose.feed.internal.components.LocalFeedImageLoader
import com.sarang.torang.di.basefeed_di.CustomExpandableTextType
import com.sarang.torang.di.basefeed_di.CustomFeedImageLoader
import com.sarang.torang.di.feed_di.CustomBottomDetectingLazyColumnType
import com.sarang.torang.di.feed_di.CustomPullToRefreshType
import com.sarang.torang.viewmodels.FeedDialogsViewModel
import com.sryang.library.pullrefresh.rememberPullToRefreshState

/**
 * 피드 화면과 프로필 화면
 * @param onAlarm 상단바에서 알림 클릭 콜백
 * @param onPage 페이지 콜백 Int: 현재 페이지, Boolean: 첫번째 페이지 여부, Boolean: 마지막 페이지 여부
 */
@Composable
fun FeedScreenWithProfile(
    rootNavController: RootNavController,
    feedNavController: NavHostController,
    dialogsViewModel: FeedDialogsViewModel,
    onTop: Boolean,
    consumeOnTop: () -> Unit,
    onAddReview: () -> Unit,
    onMessage: (Int) -> Unit,
    onAlarm: () -> Unit = { Log.w("__FeedScreenWithProfile", "onAlarm is not implemented") },
    onPage: (Int, Boolean, Boolean) -> Unit = { _, _, _ -> },
    scrollEnabled: Boolean = true,
    pageScrollable : Boolean = true
) {
    val state = rememberPullToRefreshState()
    NavHost(navController = feedNavController, startDestination = "feed") {
        composable("feed") {
            CompositionLocalProvider(
                LocalFeedCompose provides MainFeed(dialogsViewModel, feedNavController, rootNavController, onPage),
                LocalBottomDetectingLazyColumnType provides CustomBottomDetectingLazyColumnType,
                LocalPullToRefreshLayoutType provides CustomPullToRefreshType,
                LocalFeedImageLoader provides CustomFeedImageLoader,
                LocalExpandableTextType provides CustomExpandableTextType
            ){
                FeedScreenInMain(
                    onAddReview = onAddReview,
                    onAlarm = onAlarm,
                    onScrollToTop = consumeOnTop,
                    scrollToTop = onTop,
                    scrollEnabled = scrollEnabled,
                    pageScrollable = pageScrollable
                )
            }
        }
        composable(
            "profile/{id}",
            content = {
                provideProfileScreen(
                    rootNavController = rootNavController,
                    navController = feedNavController,
                    onMessage = onMessage
                ).invoke(it)
            }
        )
    }
}

fun MainFeed(
    dialogsViewModel: FeedDialogsViewModel,
    feedNavController: NavHostController,
    rootNavController: RootNavController,
    onPage: (Int, Boolean, Boolean) -> Unit = { _, _, _ -> }
): feedType = { feed, onLike, onFavorite, isLogin, onVideoClick, imageHeight, pageScrollAble ->
    provideFeed(
        dialogsViewModel = dialogsViewModel,
        navController = feedNavController,
        rootNavController = rootNavController,
        onPage = onPage
    ).invoke(feed,onLike,onFavorite,isLogin,onVideoClick, imageHeight, pageScrollAble )
}