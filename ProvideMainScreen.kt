package com.sryang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.commonwidgets.torangcomposepack.AndroidViewRatingBar
import com.sryang.findinglinkmodules.di.finding.Finding
import com.sarang.torang.BuildConfig
import com.sarang.torang.di.torang.ProvideProfileScreen
import com.sryang.torang.comments.CommentsModal
import com.sryang.torang.compose.AlarmScreen
import com.sryang.torang.compose.MainScreen
import com.sryang.torang.compose.bottomsheet.CommentBottomSheetDialog
import com.sryang.torang.compose.bottomsheet.feed.FeedMenuBottomSheetDialog
import com.sryang.torang.compose.bottomsheet.share.ShareBottomSheetDialog
import com.sryang.torang.compose.feed.FeedScreen
import com.sryang.torang.compose.feed.Feeds
import com.sryang.torang.compose.report.ReportModal
import com.sryang.torang.di.feed_di.review

@Composable
fun ProvideMainScreen(navController: NavHostController) {
    MainScreen(
        feedScreen = { onComment, onMenu, onShare, onReport, onReported ->
            FeedScreen(
                clickAddReview = { navController.navigate("addReview") },
                feeds = { list, onLike, onFavorite, onRefresh, onBottom, isRefreshing, isEmpty, isLoading ->
                    Feeds(
                        list = list.map { it.review() },
                        onProfile = { navController.navigate("profile/$it") },
                        onLike = onLike,
                        onComment = onComment,
                        onShare = onShare,
                        onFavorite = onFavorite,
                        onMenu = onMenu,
                        onName = {},
                        onRestaurant = { navController.navigate("restaurant/$it") },
                        onImage = {},
                        onRefresh = onRefresh,
                        onBottom = onBottom,
                        isRefreshing = isRefreshing,
                        isEmpty = isEmpty,
                        ratingBar = {
                            AndroidViewRatingBar(
                                rating = it,
                                isSmall = true,
                                changable = false
                            )
                        },
                        isLoading = isLoading
                    )
                },
            )
        },
        findingScreen = {
            Finding(
                navController = navController
            )
        },
        myProfileScreen = {
            ProvideProfileScreen(navController = navController)
        },
        alarm = {
            AlarmScreen(onEmailLogin = {
                navController.navigate("emailLogin")
            })
        },
        commentDialog = { reviewId, onClose ->
            CommentsModal(
                profileImageServerUrl = BuildConfig.PROFILE_IMAGE_SERVER_URL,
                reviewId = reviewId,
                onDismissRequest = onClose
            )
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