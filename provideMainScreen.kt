package com.sarang.torang.di.main_di

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainScreen
import com.sarang.torang.di.profile_di.MyProfileScreenNavHost
import com.sarang.torang.viewmodels.FeedDialogsViewModel
import com.sryang.findinglinkmodules.di.finding_di.Finding
import com.sarang.torang.compose.AlarmScreen

fun provideMainScreen(
    rootNavController: RootNavController,
    videoPlayer: @Composable (url: String, isPlaying: Boolean, onVideoClick: () -> Unit) -> Unit,
): @Composable () -> Unit = {
    val dialogsViewModel: FeedDialogsViewModel = hiltViewModel()
    val feedNavController = rememberNavController() // 메인 하단 홈버튼 클릭시 처리를 위해 여기에 설정
    var latestDestination by remember { mutableStateOf("feed") }
    var onTop by remember { mutableStateOf(false) }

    ProvideMainDialog(
        dialogsViewModel = dialogsViewModel,
        rootNavController = rootNavController
    ) {
        MainScreen(
            feedScreen = {
                FeedScreenWithProfile(
                    rootNavController = rootNavController,
                    feedNavController = feedNavController,
                    dialogsViewModel = dialogsViewModel,
                    onTop = onTop,
                    consumeOnTop = { onTop = false },
                    videoPlayer = videoPlayer
                )
            },
            onBottomMenu = {
                if (feedNavController.currentDestination?.route != "feed" && latestDestination == "feed" && it == "feed") {
                    // 피드 화면안에서 다른화면 상태일 때 피드 버튼을 눌렀다면 피드 화면으로 이동
                    feedNavController.popBackStack("feed", inclusive = false)
                } else if (latestDestination == "feed" && it == "feed") {
                    // 피드화면에서 피드 버튼을 눌렀을 때 리스트 최상단 이동
                    onTop = true
                }
                latestDestination = it
                Log.d("__provideMainScreen", "onBottomMenu:${it}")
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
                            navBackStackEntry = it,
                            videoPlayer = videoPlayer
                        )
                    }
                )
            },
            alarm = {
                AlarmScreen(
                    onEmailLogin = { rootNavController.emailLogin() },
                    onContents = { rootNavController.review(it) },
                    onProfile = { rootNavController.profile(it) })
            },
        )
    }
}