package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.commonwidgets.torangcomposepack.AndroidViewRatingBar
import com.sarang.torang.BuildConfig
import com.sarang.torang.compose.MainScreen
import com.sarang.torang.di.feed_di.review
import com.sryang.torang.compose.AlarmScreen
import com.sryang.torang.compose.bottomsheet.feed.FeedMenuBottomSheetDialog
import com.sryang.torang.compose.bottomsheet.share.ShareBottomSheetDialog
import com.sarang.torang.compose.feed.FeedScreen
import com.sarang.torang.compose.feed.Feeds
import com.sarang.torang.uistate.FeedsUiState
import com.sryang.torang.compose.report.ReportModal

@Composable
fun ProvideMainScreen(
    navController: NavHostController,
    findingScreen: @Composable (() -> Unit)? = null,
    myProfileScreen: @Composable (() -> Unit)? = null
) {
    val tag = "_ProvideMainScreen"
    MainScreen(
        feedScreen = { onComment, onMenu, onShare, onReport, onReported ->
            FeedScreen(
                onAddReview = { navController.navigate("addReview") },
                feeds = { list, onRefresh, onBottom, isRefreshing ->
                    Feeds(
                        onRefresh = onRefresh,
                        onBottom = onBottom,
                        isRefreshing = isRefreshing,
                        ratingBar = { modifier, rating ->
                            AndroidViewRatingBar(
                                modifier = modifier,
                                rating = rating,
                                isSmall = true,
                                changable = false
                            )
                        },
                        feedsUiState = FeedsUiState.Success(list.map {
                            it.review(
                                onProfile = { navController.navigate("profile/${it.userId}") },
                                onName = { navController.navigate("profile/${it.userId}") },
                                onMenu = { onMenu.invoke(it.reviewId) },
                                onShare = { onShare.invoke(it.reviewId) },
                                onComment = { onComment.invoke(it.reviewId) },
                                onRestaurant = { navController.navigate("restaurant/${it.restaurantId}") }
                            )
                        })
                    )
                },
            )
        },
        findingScreen = {
            findingScreen?.invoke()
        },
        myProfileScreen = {
            myProfileScreen?.invoke()
        },
        alarm = {
            AlarmScreen(onEmailLogin = {})
        },
        commentDialog = { reviewId, onClose ->
            /*CommentsModal(
                profileImageServerUrl = BuildConfig.PROFILE_IMAGE_SERVER_URL,
                reviewId = reviewId,
                onDismissRequest = onClose
            )*/
        },
        menuDialog = { reviewId, onClose, onReport, onDelete, onEdit ->
            FeedMenuBottomSheetDialog(
                isExpand = true,
                reviewId = reviewId,
                onReport = { onReport.invoke(reviewId) },
                onDelete = { onDelete.invoke(reviewId) },
                onEdit = { onEdit.invoke(reviewId) },
                onClose = onClose
            )
        },
        shareDialog = { onClose ->
            ShareBottomSheetDialog(
                profileServerUrl = BuildConfig.PROFILE_IMAGE_SERVER_URL,
                isExpand = true,
                onSelect = {},
                onClose = onClose
            )
        },
        reportDialog = { it, onReported ->
            ReportModal(
                reviewId = it,
                profileServerUrl = BuildConfig.PROFILE_IMAGE_SERVER_URL,
                onReported = onReported
            )
        },
        onEdit = { navController.navigate("modReview/${it}") }
    )
}