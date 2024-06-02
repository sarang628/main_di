package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainDialogs
import com.sarang.torang.di.bottomsheet.provideFeedMenuBottomSheetDialog
import com.sarang.torang.di.report_di.provideReportModal
import com.sarang.torang.di.torang.provideCommentBottomDialogSheet
import com.sarang.torang.viewmodels.FeedDialogsViewModel
import com.sryang.torangbottomsheet.di.bottomsheet.provideShareBottomSheetDialog

@Composable
fun ProvideMainDialog(
    dialogsViewModel: FeedDialogsViewModel = hiltViewModel(),
    rootNavController: RootNavController,
    contents: @Composable () -> Unit,
) {
    val uiState by dialogsViewModel.uiState.collectAsState()
    MainDialogs(
        uiState = uiState,
        commentBottomSheet = { provideCommentBottomDialogSheet(rootNavController).invoke(it) { dialogsViewModel.closeComment() } },
        menuDialog = provideFeedMenuBottomSheetDialog(),
        shareDialog = provideShareBottomSheetDialog(),
        reportDialog = provideReportModal(),
        onEdit = rootNavController.modReview(),
        contents = contents
    )
}
