package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.FeedScreenByReviewId
import com.sarang.torang.di.feed_di.provideBottomDetectingLazyColumn
import com.sarang.torang.di.feed_di.shimmerBrush
import com.sarang.torang.di.video.provideVideoPlayer
import com.sarang.torang.viewmodels.FeedDialogsViewModel
import com.sryang.library.pullrefresh.PullToRefreshLayout
import com.sryang.library.pullrefresh.PullToRefreshLayoutState
import com.sryang.library.pullrefresh.RefreshIndicatorState

fun provideFeedScreen(
    rootNavController: RootNavController,
    state: PullToRefreshLayoutState,
    reviewId: Int
): @Composable (Int) -> Unit =
    {
        val dialogsViewModel: FeedDialogsViewModel = hiltViewModel()
        ProvideMainDialog(
            dialogsViewModel = dialogsViewModel,
            rootNavController = rootNavController,
            commentBottomSheet = provideCommentBottomDialogSheet(
                rootNavController
            )
        ) {
            FeedScreenByReviewId(
                reviewId = reviewId,
                shimmerBrush = { shimmerBrush(it) },
                feed = provideFeed(
                    dialogsViewModel = dialogsViewModel,
                    navController = rootNavController.navController,
                    rootNavController = rootNavController,
                    videoPlayer = provideVideoPlayer()
                ),
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
                },
                bottomDetectingLazyColumn = provideBottomDetectingLazyColumn()
            )
        }
    }