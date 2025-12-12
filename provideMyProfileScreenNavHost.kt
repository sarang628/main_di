package com.sarang.torang.di.main_di

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.sarang.torang.RootNavController
import com.sarang.torang.di.chat_di.ChatActivity
import com.sarang.torang.di.profile_di.MyProfileScreenNavHost

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal fun provideMyProfileScreenNavHost(rootNavController: RootNavController): @Composable () -> Unit =
    {
        val context = LocalContext.current
        val profileNavController = rememberNavController() // 상위에 선언하면 앱 죽음
        //Scaffold { // 여기에 만들면 메인 앱에서 상하단 패딩 생김
            //Box(Modifier.padding(it)){
                MyProfileScreenNavHost(
                    navController = profileNavController,
                    onSetting = rootNavController::settings,
                    onEmailLogin = rootNavController::emailLogin,
                    onReview = { profileNavController.navigate("myFeed/${it}") },
                    onClose = profileNavController::popBackStack,
                    onMessage = { ChatActivity.go(context, it) },
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                )
            //}
        //}
    }
