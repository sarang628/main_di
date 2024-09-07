package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainDialogs
import com.sarang.torang.di.bottomsheet.provideFeedMenuBottomSheetDialog
import com.sarang.torang.di.report_di.provideReportModal
import com.sarang.torang.viewmodels.FeedDialogsViewModel
import com.sryang.torangbottomsheet.di.bottomsheet.provideShareBottomSheetDialog

/**
 * 메인 다이얼로그 제공
 *
 * 메인 화면에서 사용하는 다이얼로그
 * 리뷰 신고, 리뷰 메뉴, 리뷰 공유, 리뷰 댓글 보기/작성 다이얼로그
 * @param dialogsViewModel 다이얼로그 뷰모델
 * @param rootNavController 루트 네비게이션 컨트롤러
 * @param contents 화면
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
        menuDialog = provideFeedMenuBottomSheetDialog(),
        shareDialog = provideShareBottomSheetDialog(),
        reportDialog = provideReportModal(),
        onEdit = rootNavController.modReview(),
        contents = contents
    )
}
