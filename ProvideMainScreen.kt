package com.sryang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sarang.alarm.compose.AlarmScreen
import com.sryang.main.BuildConfig
import com.sryang.screenfindingtest.di.finding.Finding
import com.sryang.torang.compose.MainScreen
import com.sryang.torang.compose.bottomsheet.comment.CommentBottomSheetDialog
import com.sryang.torang.compose.bottomsheet.feed.FeedMenuBottomSheetDialog
import com.sryang.torang.compose.bottomsheet.share.ShareBottomSheetDialog
import com.sryang.torang.compose.report.ReportModal
import com.sryang.torang.di.feed.FeedScreen

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
        commentDialog = { onClose ->
            CommentBottomSheetDialog(
                profileImageServerUrl = "",
                profileImageUrl = "",
                list = listOf(),
                isExpand = true,
                onSelect = {},
                onClose = onClose,
                onSend = {},
                name = "name"
            )
        },
        menuDialog = { reviewId, onClose, onReport ->
            FeedMenuBottomSheetDialog(
                isExpand = true,
                isMine = false,
                onReport = { onReport.invoke(reviewId) },
                onDelete = { /*TODO*/ },
                onEdit = { /*TODO*/ }, onClose = onClose
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
        }
    )
}