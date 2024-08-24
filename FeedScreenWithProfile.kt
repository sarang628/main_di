package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.FeedScreenForMain
import com.sarang.torang.di.feed_di.shimmerBrush
import com.sarang.torang.viewmodels.FeedDialogsViewModel
import com.sryang.library.pullrefresh.PullToRefreshLayout
import com.sryang.library.pullrefresh.RefreshIndicatorState
import com.sryang.library.pullrefresh.rememberPullToRefreshState

@Composable
fun FeedScreenWithProfile(
    rootNavController: RootNavController,
    feedNavController: NavHostController,
    dialogsViewModel: FeedDialogsViewModel,
    onTop: Boolean,
    consumeOnTop: () -> Unit,
) {
    val state = rememberPullToRefreshState()
    NavHost(navController = feedNavController, startDestination = "feed") {
        composable("feed") {
            FeedScreenForMain(
                onAddReview = { rootNavController.addReview() },
                feed = provideFeed(
                    { dialogsViewModel.onComment(it) },
                    { dialogsViewModel.onMenu(it) },
                    { dialogsViewModel.onShare(it) },
                    navController = feedNavController,
                    rootNavController = rootNavController
                ),
                shimmerBrush = { shimmerBrush(it) },
                consumeOnTop = consumeOnTop,
                onTop = onTop,
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
                    navController = feedNavController
                ).invoke(it)
            }
        )
    }
}