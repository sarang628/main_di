package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.sarang.torang.compose.MainMyFeedScreen
import com.sarang.torang.di.bottomsheet.provideFeedMenuBottomSheetDialog
import com.sarang.torang.di.comment_di.provideCommentBottomDialogSheet
import com.sarang.torang.di.feed_di.provideMyFeedScreen
import com.sarang.torang.di.report_di.provideReportModal
import com.sryang.torangbottomsheet.di.bottomsheet.provideShareBottomSheetDialog

@Composable
fun ProvideMyFeedScreen(
    navController: NavHostController,
    reviewId : Int,
    onEdit : (Int) -> Unit,
    onProfile : ((Int) -> Unit)? = null,
    onBack: (() -> Unit)? = null
) {
    var show by remember { mutableStateOf(false) }

    MainMyFeedScreen(
        myFeedScreen = provideMyFeedScreen(navController = navController, reviewId = reviewId, onShowComment = {show = true }, onProfile = onProfile, onBack = onBack),
        commentBottomSheet = provideCommentBottomDialogSheet(show) { show = false },
        menuDialog = provideFeedMenuBottomSheetDialog(),
        shareDialog = provideShareBottomSheetDialog(),
        reportDialog = provideReportModal(),
        onEdit = onEdit
    )
}