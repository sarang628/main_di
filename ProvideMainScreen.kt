package com.sarang.torang.di.main_di

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.sarang.torang.BuildConfig
import com.sarang.torang.compose.MainScreen
import com.sarang.torang.compose.feed.Feeds
import com.sarang.torang.di.comment_di.CommentBottomSheet
import com.sarang.torang.di.feed_di.ProvideFeedScreen
import com.sarang.torang.di.feed_di.ProvideMyFeedScreen
import com.sarang.torang.di.feed_di.review
import com.sarang.torang.di.profile_di.ProfileScreen
import com.sarang.torang.uistate.FeedUiState
import com.sarang.torang.uistate.FeedsUiState
import com.sryang.findinglinkmodules.di.finding_di.Finding
import com.sryang.torang.compose.AlarmScreen
import com.sryang.torang.compose.bottomsheet.feed.FeedMenuBottomSheetDialog
import com.sryang.torang.compose.bottomsheet.share.ShareBottomSheetDialog
import com.sryang.torang.compose.report.ReportModal

@Composable
fun ProvideMainScreen(
    navController: NavHostController,
    onBackPressed: (() -> Unit)? = null
) {
    var show by remember { mutableStateOf(false) }

    MainScreen(
        feedScreen = { onComment, onMenu, onShare ->
            ProvideFeedScreen(onAddReview = { navController.navigate("addReview") },
                onProfile = { userId -> navController.navigate("profile/${userId}") },
                onName = { userId -> navController.navigate("profile/${userId}") },
                onMenu = { reviewId -> onMenu.invoke(reviewId) },
                onShare = { reviewId -> onShare.invoke(reviewId) },
                onComment = { reviewId ->
                    show = true
                    onComment.invoke(reviewId)
                },
                onRestaurant = { restaurantId -> navController.navigate("restaurant/${restaurantId}") })
        },
        findingScreen = {
            Finding(
                navController = navController
            )
        },
        myProfileScreen = {
            ProfileScreen(
                onSetting = { navController.navigate("settings") },
                navBackStackEntry = null,
                onClose = { navController.popBackStack() },
                onEmailLogin = { navController.navigate("emailLogin") },
                onReview = {
                    Log.d("__Main", "reviewId : ${it}")
                    navController.navigate("myFeed/${it}")
                }
            )
        },
        alarm = {
            AlarmScreen(onEmailLogin = {})
        },
        commentBottomSheet = { reviewId, onDismissRequest, onBackPressed, content ->
            CommentBottomSheet(
                reviewId = reviewId,
                onDismissRequest = onDismissRequest,
                show = show,
                onHidden = {
                    show = false
                },
                content = content
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
        onEdit = { navController.navigate("modReview/${it}") },
        onBackPressed = onBackPressed
    )
}