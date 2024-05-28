package com.sarang.torang.di.main_di

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.rememberNavController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainScreen
import com.sarang.torang.di.profile_di.MyProfileScreenNavHost
import com.sarang.torang.viewmodels.FeedDialogsViewModel
import com.sryang.findinglinkmodules.di.finding_di.Finding
import com.sryang.torang.compose.AlarmScreen

fun provideMainScreen(
    rootNavController: RootNavController,
): @Composable (NavBackStackEntry) -> Unit = {
    val dialogsViewModel: FeedDialogsViewModel = hiltViewModel()
    ProvideMainDialog(
        dialogsViewModel = dialogsViewModel,
        navController = rootNavController
    ) {
        MainScreen(
            feedScreen = {
                FeedScreenWithProfile(rootNavController = rootNavController, dialogsViewModel)
            },
            onBottomMenu = {
                Log.d("__MainActivity", "onBottomMenu:${it}")
            },
            findingScreen = { Finding(navController = rootNavController) },
            myProfileScreen = {
                val profileNavController = rememberNavController() // 상위에 선언하면 앱 죽음
                MyProfileScreenNavHost(
                    navController = profileNavController,
                    onSetting = { rootNavController.settings() },
                    onEmailLogin = { rootNavController.emailLogin() },
                    onReview = {
                        Log.d(
                            "__Main",
                            "MyProfileScreen onReview reviewId : ${it}"
                        )
                        profileNavController.navigate("myFeed/${it}")
                    },
                    onClose = { profileNavController.popBackStack() },
                    myFeed = {
                        ProvideMyFeedScreen(
                            rootNavController = rootNavController,
                            navController = profileNavController,
                            navBackStackEntry = it
                        )
                    }
                )
            },
            alarm = { AlarmScreen(onEmailLogin = {}) },
        )
    }
}