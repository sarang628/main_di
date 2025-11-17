package com.sarang.torang.di.main_di

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainDialogs
import com.sarang.torang.compose.type.LocalCommentBottomSheet
import com.sarang.torang.compose.type.LocalMenuBottomSheet
import com.sarang.torang.compose.type.LocalReportBottomSheetType
import com.sarang.torang.compose.type.LocalRestaurantBottomSheet
import com.sarang.torang.compose.type.LocalShareBottomSheet
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
    CompositionLocalProvider(
        LocalCommentBottomSheet provides { reviewId ->
            provideCommentBottomDialogSheet(rootNavController).invoke(
                CommentBottomDialogSheetData(
                    reviewId = reviewId,
                    onHidden = { dialogsViewModel.closeComment() },
                    content  = content
                )
            )
        },
        LocalMenuBottomSheet provides provideFeedMenuBottomSheetDialog(),
        LocalShareBottomSheet provides provideShareBottomSheetDialog(uiState.showShare),
        LocalRestaurantBottomSheet provides restaurantBottomSheet,
        LocalReportBottomSheetType provides provideReportModal()
    ) {
        MainDialogs(
            uiState = uiState,
            onEdit = rootNavController.modReview(),
        )
    }
}
