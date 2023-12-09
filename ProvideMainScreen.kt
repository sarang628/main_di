package com.sryang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sarang.alarm.compose.AlarmScreen
import com.sryang.main.BuildConfig
import com.sryang.screenfindingtest.di.finding.Finding
import com.sryang.torang.compose.MainScreen
import com.sryang.torang.compose.bottomsheet.CommentBottomSheetDialog
import com.sryang.torang.compose.bottomsheet.feed.FeedMenuBottomSheetDialog
import com.sryang.torang.compose.bottomsheet.share.ShareBottomSheetDialog
import com.sryang.torang.compose.report.ReportModal
import com.sryang.torang.di.feed_di.FeedScreen

private val profileImageServerUrl = BuildConfig.PROFILE_IMAGE_SERVER_URL
private val imageServerUrl = "http://sarang628.iptime.org:89/review_images/"

@Composable
fun ProvideMainScreen(navController: NavHostController) {
    MainScreen(
        feedScreen = { onComment, onMenu, onShare, onReport, onReported ->
            FeedScreen(
                onProfile = { navController.navigate("profile/$it") },
                onRestaurant = { navController.navigate("restaurant/$it") },
                onImage = {},
                onName = {},
                clickAddReview = { navController.navigate("addReview") },
                profileImageServerUrl = profileImageServerUrl,
                imageServerUrl = imageServerUrl,
                ratingBar = {},
                onComment = onComment,
                onMenu = onMenu,
                onShare = onShare,
                onReport = onReport,
                onReported = onReported
            )
        },
        findingScreen = {
            Finding(
                navController = navController
            )
        },
        myProfileScreen = {
        },
        alarm = { AlarmScreen(profileServerUrl = profileImageServerUrl) },
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
                profileServerUrl = profileImageServerUrl,
                isExpand = true,
                onSelect = {},
                onClose = onClose
            )
        },
        reportDialog = { it, onReported ->
            ReportModal(
                reviewId = it,
                profileServerUrl = profileImageServerUrl,
                onReported = onReported
            )
        },
        onDelete = { navController.navigate("modReview/${it}") },
        onEdit = {}
    )
}