package com.sarang.torang.di.main_di

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.sarang.torang.dialogsbox.compose.DialogsBoxViewModel
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.FeedScreenByReviewId
import com.sarang.torang.di.comment_di.CommentBottomDialogSheetData
import com.sarang.torang.di.dialogsbox_di.ProvideDialogsBox
import com.sryang.library.pullrefresh.rememberPullToRefreshState

@Composable
fun ProvideMyFeedScreen(
    dialogsViewModel: DialogsBoxViewModel = hiltViewModel(),
    navController: NavHostController,
    rootNavController: RootNavController,
    navBackStackEntry: NavBackStackEntry,
) {
    val reviewId: Int? = navBackStackEntry.arguments?.getString("reviewId")?.toInt()
    val state = rememberPullToRefreshState()

    if (reviewId == null) {
        Log.e("__ProvideMyFeedScreen", "reviewId is null. can't load FeedScreen")
        return
    }

    ProvideDialogsBox(
        dialogsViewModel = dialogsViewModel,
        rootNavController = rootNavController,
    ) {
        FeedScreenByReviewId(
            reviewId = reviewId,
        )
    }
}