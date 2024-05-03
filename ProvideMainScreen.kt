package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.sarang.torang.compose.MainScreen
import com.sarang.torang.di.bottomsheet.provideFeedMenuBottomSheetDialog
import com.sarang.torang.di.comment_di.provideCommentBottomDialogSheet
import com.sarang.torang.di.feed_di.provideFeedScreen
import com.sarang.torang.di.profile_di.provideProfileScreen
import com.sarang.torang.di.report_di.provideReportModal
import com.sryang.findinglinkmodules.di.finding_di.Finding
import com.sryang.torang.compose.AlarmScreen
import com.sryang.torangbottomsheet.di.bottomsheet.provideShareBottomSheetDialog

@Composable
fun ProvideMainScreen(
    navController: NavHostController
) {
    var show by remember { mutableStateOf(false) }

    MainScreen(
        feedScreen = provideFeedScreen(navController = navController) { show = true },
        findingScreen = { Finding(navController = navController) },
        myProfileScreen = provideProfileScreen(navController),
        alarm = { AlarmScreen(onEmailLogin = {}) },
        commentBottomSheet = provideCommentBottomDialogSheet(show) { show = false },
        menuDialog = provideFeedMenuBottomSheetDialog(),
        shareDialog = provideShareBottomSheetDialog(),
        reportDialog = provideReportModal(),
        onEdit = { navController.navigate("modReview/${it}") }
    )
}