package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.sarang.torang.FeedDialogsViewModel
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.FeedScreenByRestaurantId
import com.sarang.torang.di.dialogsbox_di.ProvideMainDialog
import com.sryang.library.pullrefresh.rememberPullToRefreshState

fun provideFeedScreenByRestaurantId(rootNavController: RootNavController): @Composable (Int) -> Unit =
    {
        val dialogsViewModel: FeedDialogsViewModel = hiltViewModel()
        val state = rememberPullToRefreshState()
        ProvideMainDialog(
            rootNavController = rootNavController,
            dialogsViewModel = dialogsViewModel,
            //commentBottomSheet = provideCommentBottomDialogSheet(rootNavController)
        ) {
            FeedScreenByRestaurantId(
                restaurantId = 1,
                /*shimmerBrush = { it -> shimmerBrush(it) },
                feed = { feed, onLike, onFavorite, isLogin, onVideoClick, imageHeight, pageScrollable ->
                    Feed(
                        review = feed.toReview(),
                        imageLoadCompose = provideZoomableTorangAsyncImage(),
                        onComment = { dialogsViewModel.onComment(feed.reviewId) },
                        onShare = { dialogsViewModel.onShare(feed.reviewId) },
                        onMenu = { dialogsViewModel.onMenu(feed.reviewId) },
                        onImage = { rootNavController.imagePager(feed.reviewId, it) },
                        onLike = { onLike.invoke(feed.reviewId) },
                        onFavorite = { onFavorite.invoke(feed.reviewId) },
                        onRestaurant = {
                            Log.w("provideFeedScreenByRestaurantId", "onRestaurant is nothing")
                        },
                        onName = {
                            Log.w("provideFeedScreenByRestaurantId", "onName is nothing")
                        },
                        onProfile = {
                            Log.w("provideFeedScreenByRestaurantId", "onProfile is nothing")
                        },
                        isZooming = {
                            Log.w("provideFeedScreenByRestaurantId", "isZooming is nothing")
                        },
                        onLikes = { rootNavController.like(feed.reviewId) },
                        expandableText = provideExpandableText(),
                        videoPlayer = {
                            VideoPlayerScreen(
                                videoUrl = it,
                                feed.isPlaying,
                                onClick = onVideoClick,
                                onPlay = {})
                        },
                        imageHeight = if (imageHeight > 0) imageHeight.dp else 600.dp
                    )
                },
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
                bottomDetectingLazyColumn = provideBottomDetectingLazyColumn()*/
            )
        }
    }