package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainDialogs
import com.sarang.torang.di.report_di.provideReportModal
import com.sarang.torang.di.report_di.provideShareBottomSheetDialog
import com.sarang.torang.viewmodels.FeedDialogsViewModel

/**
 * 메인 다이얼로그
 *
 * 메인 화면에서 필요한 다이얼로그 종류:
 * 댓글 보기, 공유, 신고, 메뉴 4가지가 있다.
 *
 * 각 다이얼로그는 다른 모듈에서 가져온다.
 *
 */
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
        menuDialog = { _,_,_,_,_-> },
        shareDialog = provideShareBottomSheetDialog(),
        reportDialog = provideReportModal(),
        onEdit = rootNavController.modReview(),
        contents = contents
    )
}
