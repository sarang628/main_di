package com.sarang.torang.di.main_di

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.FeedScreenForMain
import com.sarang.torang.di.feed_di.shimmerBrush
import com.sarang.torang.viewmodels.FeedDialogsViewModel
import com.sryang.library.pullrefresh.PullToRefreshLayout
import com.sryang.library.pullrefresh.RefreshIndicatorState
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
    videoPlayer: @Composable (url: String, isPlaying: Boolean, onVideoClick: () -> Unit) -> Unit,
    onAddReview: (() -> Unit),
    onMessage: (Int) -> Unit,
    onAlarm: () -> Unit = { Log.w("__FeedScreenWithProfile", "onAlarm is not implemented") },
    onPage: (Int, Boolean, Boolean) -> Unit = { _, _, _ -> }
) {
    val state = rememberPullToRefreshState()
    NavHost(navController = feedNavController, startDestination = "feed") {
        composable("feed") {
            FeedScreenForMain(
                onAddReview = onAddReview,
                onAlarm = onAlarm,
                feed = provideFeed(
                    { dialogsViewModel.onComment(it) },
                    { dialogsViewModel.onMenu(it) },
                    { dialogsViewModel.onShare(it) },
                    navController = feedNavController,
                    rootNavController = rootNavController,
                    videoPlayer = videoPlayer,
                    onPage = onPage
                ),
                shimmerBrush = { shimmerBrush(it) },
                onScrollToTop = consumeOnTop,
                scrollToTop = onTop,
                pullToRefreshLayout = { isRefreshing, onRefresh, contents ->

                    if (isRefreshing) {
                        state.updateState(RefreshIndicatorState.Refreshing)
                    } else {
                        state.updateState(RefreshIndicatorState.Default)
                    }

                    PullToRefreshLayout(
                        pullRefreshLayoutState = state,
                        refreshThreshold = 80,
                        onRefresh = onRefresh
                    ) {
                        contents.invoke()
                    }
                }
            )
        }
        composable(
            "profile/{id}",
            content = {
                provideProfileScreen(
                    rootNavController = rootNavController,
                    navController = feedNavController,
                    videoPlayer = videoPlayer,
                    onMessage = onMessage
                ).invoke(it)
            }
        )
    }
}