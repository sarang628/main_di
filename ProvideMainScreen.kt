package com.sryang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sryang.findinglinkmodules.di.finding.Finding
import com.sarang.torang.BuildConfig
import com.sryang.torang.compose.AlarmScreen
import com.sryang.torang.compose.MainScreen
import com.sryang.torang.compose.bottomsheet.CommentBottomSheetDialog
import com.sryang.torang.compose.bottomsheet.feed.FeedMenuBottomSheetDialog
import com.sryang.torang.compose.bottomsheet.share.ShareBottomSheetDialog
import com.sryang.torang.compose.feed.FeedScreen
import com.sryang.torang.compose.feed.Feeds
import com.sryang.torang.compose.report.ReportModal
import com.sryang.torang.di.feed_di.review

private val imageServerUrl = "http://sarang628.iptime.org:89/review_images/"

@Composable
fun ProvideMainScreen(navController: NavHostController) {
    MainScreen(
        feedScreen = { onComment, onMenu, onShare, onReport, onReported ->
            FeedScreen(
                clickAddReview = { navController.navigate("addReview") },
                feeds = { list, onLike, onFavorite, onRefresh, onBottom, isRefreshing, isEmpty ->
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
                        ratingBar = {}
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
            //ProvideProfileScreen(navController = navController)
        },
        alarm = { AlarmScreen() },
        commentDialog = { reviewId, onClose ->
            CommentBottomSheetDialog(
                isExpand = true,
                onClose = onClose,
                commentList = {}
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