package com.sarang.torang.di.main_di

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainDialogs
import com.sarang.torang.di.bottomsheet_di.provideFeedMenuBottomSheetDialog
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
 * @param menuDialog 메뉴 다이얼로그 기본 bottomsheet_di 모듈에서 가져옴
 * @param shareDialog 공유 다이얼로그 기본 repoirt_di 모듈에서 가져옴
 * @param reportDialog 신고 다이얼로그 기본 repoirt_di 모듈에서 가져옴
 * @param contents 메인 화면의 컨텐츠를 이곳에 넣는다.
 *
 */
@Composable
fun ProvideMainDialog(
    dialogsViewModel: FeedDialogsViewModel = hiltViewModel(),
    rootNavController: RootNavController,
    menuDialog: @Composable (reviewId: Int, onClose: () -> Unit, onReport: (Int) -> Unit, onDelete: (Int) -> Unit, onEdit: (Int) -> Unit) -> Unit = provideFeedMenuBottomSheetDialog(),
    shareDialog: @Composable (onClose: () -> Unit) -> Unit = provideShareBottomSheetDialog(),
    reportDialog: @Composable (Int, onReported: () -> Unit) -> Unit = provideReportModal(),
    commentBottomSheet: @Composable (
        reviewId: Int?, onHidden: () -> Unit, content: @Composable (PaddingValues) -> Unit
    ) -> Unit,
    contents: @Composable (PaddingValues) -> Unit
) {
    val uiState by dialogsViewModel.uiState.collectAsState()
    MainDialogs(
        uiState = uiState,
        commentBottomSheet = { reviewId ->
            commentBottomSheet.invoke(reviewId, { dialogsViewModel.closeComment() }, contents)
        },
        menuDialog = menuDialog,
        shareDialog = shareDialog,
        reportDialog = reportDialog,
        onEdit = rootNavController.modReview()
    )
}
