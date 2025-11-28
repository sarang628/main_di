package com.sarang.torang.di.main_di

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.sarang.torang.RootNavController
import com.sarang.torang.di.chat_di.ChatActivity
import com.sarang.torang.di.comment_di.provideCommentBottomDialogSheet
import com.sarang.torang.di.profile_di.MyProfileScreenNavHost
import com.sarang.torang.di.video.provideVideoPlayer

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal fun provideMyProfileScreenNavHost(rootNavController: RootNavController): @Composable () -> Unit =
    {
        val context = LocalContext.current
        val profileNavController = rememberNavController() // 상위에 선언하면 앱 죽음
        MyProfileScreenNavHost(
            navController = profileNavController,
            onSetting = rootNavController::settings,
            onEmailLogin = rootNavController::emailLogin,
            onReview = { profileNavController.navigate("myFeed/${it}") },
            onClose = profileNavController::popBackStack,
            myFeed = {
                ProvideMyFeedScreen(
                    rootNavController = rootNavController,
                    navController = profileNavController,
                    navBackStackEntry = it
                )
            },
            onMessage = { ChatActivity.go(context, it) }

        )
    }
