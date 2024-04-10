package com.sarang.torang.di.main_di

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sarang.torang.BuildConfig
import com.sarang.torang.compose.MainScreen
import com.sarang.torang.compose.comments.CommentBottomSheet
import com.sarang.torang.compose.feed.FeedScreen
import com.sarang.torang.compose.feed.Feeds
import com.sarang.torang.di.feed_di.review
import com.sarang.torang.uistate.FeedUiState
import com.sarang.torang.uistate.FeedsUiState
import com.sryang.findinglinkmodules.di.finding_di.Finding
import com.sryang.myapplication.di.profile_di.ProfileScreen
import com.sryang.torang.compose.AlarmScreen
import com.sryang.torang.compose.bottomsheet.feed.FeedMenuBottomSheetDialog
import com.sryang.torang.compose.bottomsheet.share.ShareBottomSheetDialog
import com.sryang.torang.compose.report.ReportModal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvideMainScreen(
    navController: NavHostController,
    onBackPressed: (() -> Unit)? = null
) {
    MainScreen(
        feedScreen = { onComment, onMenu, onShare, onReport, onReported ->
            FeedScreen(
                onAddReview = { navController.navigate("addReview") },
                feeds = { uiState, onRefresh, onBottom, isRefreshing ->
                    when (uiState) {
                        is FeedUiState.Success -> {
                            Feeds(
                                onRefresh = onRefresh,
                                onBottom = onBottom,
                                isRefreshing = isRefreshing,
                                feedsUiState = FeedsUiState.Success(uiState.list.map {
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
                        }

                        is FeedUiState.Loading -> {
                            Feeds(
                                onRefresh = onRefresh,
                                onBottom = onBottom,
                                isRefreshing = isRefreshing,
                                feedsUiState = FeedsUiState.Loading
                            )
                        }

                        is FeedUiState.Error -> {

                        }
                    }
                    if (uiState is FeedUiState.Success) {

                    }
                },
            )
        },
        findingScreen = {
            Finding(navController = navController)
        },
        myProfileScreen = {
            ProfileScreen(
                onSetting = { navController.navigate("settings") },
                navBackStackEntry = null,
                onClose = { navController.popBackStack() },
                onEmailLogin = { navController.navigate("emailLogin") }
            )
        },
        alarm = {
            AlarmScreen(onEmailLogin = {})
        },
        commentBottomSheet = { reviewId, onDismissRequest, sheetState, onBackPressed, content ->
            CommentBottomSheet(
                reviewId = reviewId,
                sheetState = sheetState,
                onDismissRequest = onDismissRequest,
                onBackPressed = onBackPressed,
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