package com.sarang.torang.di.main_di

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.sarang.torang.FeedDialogsViewModel
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.FeedScreenByReviewId
import com.sarang.torang.di.comment_di.CommentBottomDialogSheetData
import com.sarang.torang.di.dialogsbox_di.ProvideMainDialog
import com.sryang.library.pullrefresh.rememberPullToRefreshState

@Composable
fun ProvideMyFeedScreen(
    dialogsViewModel: FeedDialogsViewModel = hiltViewModel(),
    navController: NavHostController,
    rootNavController: RootNavController,
    navBackStackEntry: NavBackStackEntry,
    videoPlayer: @Composable (url: String, isPlaying: Boolean, onVideoClick: () -> Unit) -> Unit,
    commentBottomSheet: @Composable (commentBottomDialogSheetData: CommentBottomDialogSheetData) -> Unit,
) {
    val reviewId: Int? = navBackStackEntry.arguments?.getString("reviewId")?.toInt()
    val state = rememberPullToRefreshState()

    if (reviewId == null) {
        Log.e("__ProvideMyFeedScreen", "reviewId is null. can't load FeedScreen")
        return
    }

    ProvideMainDialog(
        dialogsViewModel = dialogsViewModel,
        rootNavController = rootNavController,
        //commentBottomSheet = commentBottomSheet
    ) {
        FeedScreenByReviewId(
            reviewId = reviewId,
            //listState = rememberLazyListState(),
            /*feed = { feed, onLike, onFavorite, isLogin, onVideoClick, imageHeight, pageScrollable ->
                Feed(
                    review = feed.toReview(),
                    isZooming = { */
            /*scrollEnabled = !it*/
            /* },
                                imageLoadCompose = provideZoomableTorangAsyncImage(),
                                onComment = { dialogsViewModel.onComment(feed.reviewId) },
                                onShare = { if (isLogin) dialogsViewModel.onShare(feed.reviewId) },
                                onMenu = { dialogsViewModel.onMenu(feed.reviewId) },
                                onName = { navController.navigate("profile/${feed.userId}") },
                                onRestaurant = { rootNavController.restaurant(feed.restaurantId) },
                                onImage = { rootNavController.imagePager(feed.reviewId, 0) },
                                onProfile = { navController.navigate("profile/${feed.userId}") },
                                onLike = { onLike.invoke(feed.reviewId) },
                                onFavorite = { onFavorite.invoke(feed.reviewId) },
                                onLikes = { rootNavController.like(feed.reviewId) },
                                expandableText = provideExpandableText(),
                                videoPlayer = { videoPlayer.invoke(it, feed.isPlaying, onVideoClick) },
                                imageHeight = if (imageHeight > 0) imageHeight.dp else 600.dp,
                                pageScrollAble = pageScrollable
                            )
                        },*/
            //onBack = { navController.popBackStack() },
            /*pullToRefreshLayout = { isRefreshing, onRefresh, contents ->

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