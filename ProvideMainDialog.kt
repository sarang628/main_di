package com.sarang.torang.di.main_di

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainDialogs
import com.sarang.torang.di.report_di.provideReportModal
import com.sarang.torang.di.report_di.provideShareBottomSheetDialog
import com.sarang.torang.viewmodels.FeedDialogsViewModel

@Composable
fun ProvideMainDialog(
    dialogsViewModel: FeedDialogsViewModel = hiltViewModel(),
    rootNavController: RootNavController,
    commentBottomSheet: @Composable (
        reviewId: Int?, onHidden: () -> Unit, content: @Composable (PaddingValues) -> Unit
    ) -> Unit,
    contents: @Composable (PaddingValues) -> Unit
) {
    val tag = "__ProvideMainDialog"
    val uiState by dialogsViewModel.uiState.collectAsState()
    MainDialogs(
        uiState = uiState,
        commentBottomSheet = { reviewId ->
            commentBottomSheet.invoke(reviewId, { dialogsViewModel.closeComment() }, contents)
        },
        menuDialog = { _, _, _, _, _ ->
            Log.d(tag, "menuDialog isn't setting")
            dialogsViewModel.closeMenu()
        },
        shareDialog = provideShareBottomSheetDialog(),
        reportDialog = provideReportModal(),
        onEdit = rootNavController.modReview(),
    )
}
