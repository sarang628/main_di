package com.sarang.torang.di.main_di

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainDialogs
import com.sarang.torang.di.bottomsheet_di.provideFeedMenuBottomSheetDialog
import com.sarang.torang.di.report_di.provideReportModal
import com.sarang.torang.di.report_di.provideShareBottomSheetDialog
import com.sarang.torang.viewmodel.FeedDialogsViewModel

@Composable
fun ProvideMainDialog(
    dialogsViewModel        : FeedDialogsViewModel = hiltViewModel(),
    rootNavController       : RootNavController,
    restaurantBottomSheet   : @Composable ( @Composable () -> Unit ) -> Unit     = { },
    content                 : @Composable (PaddingValues) -> Unit
) {
    val tag = "__ProvideMainDialog"
    val uiState by dialogsViewModel.uiState.collectAsState()
    MainDialogs(
        uiState = uiState,
        commentBottomSheet = { reviewId ->
            provideCommentBottomDialogSheet(rootNavController).invoke(
                CommentBottomDialogSheetData(
                    reviewId = reviewId,
                    onHidden = { dialogsViewModel.closeComment() },
                    content  = content
                )
            )
        },
        menuBottomSheet = provideFeedMenuBottomSheetDialog(),
        shareBottomSheet = provideShareBottomSheetDialog(uiState.showShare),
        reportBottomSheet = provideReportModal(),
        restaurantBottomSheet = restaurantBottomSheet,
        onEdit = rootNavController.modReview(),
    )
}
