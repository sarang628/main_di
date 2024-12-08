package com.sarang.torang.di.main_di

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainScreen
import com.sarang.torang.compose.main.Feed
import com.sarang.torang.di.profile_di.MyProfileScreenNavHost
import com.sarang.torang.viewmodels.FeedDialogsViewModel
import com.sryang.findinglinkmodules.di.finding_di.Finding

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun provideMainScreen(
    rootNavController: RootNavController,
    videoPlayer: @Composable (url: String, isPlaying: Boolean, onVideoClick: () -> Unit) -> Unit,
    addReviewScreen: @Composable (onClose: () -> Unit) -> Unit,
    chat: @Composable () -> Unit,
    onCloseReview: (() -> Unit),
    onMessage: (Int) -> Unit
): @Composable () -> Unit = {
    val dialogsViewModel: FeedDialogsViewModel = hiltViewModel()
    val feedNavController = rememberNavController() // 메인 하단 홈버튼 클릭시 처리를 위해 여기에 설정
    var latestDestination: Any by remember { mutableStateOf(Feed) }
    var onTop by remember { mutableStateOf(false) }
    val TAG = "__provideMainScreen"

    ProvideMainDialog(
        dialogsViewModel = dialogsViewModel,
        rootNavController = rootNavController
    ) {
        MainScreen(
            feedScreen = { onAddReview ->
                FeedScreenWithProfile(
                    rootNavController = rootNavController,
                    feedNavController = feedNavController,
                    dialogsViewModel = dialogsViewModel,
                    onTop = onTop,
                    consumeOnTop = { onTop = false },
                    videoPlayer = videoPlayer,
                    onAddReview = onAddReview,
                    onMessage = onMessage
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
                Log.d(TAG, "onBottomMenu:${it}")
            },
            feedGrid = provideFeedGreed(),
            myProfileScreen = {
                val profileNavController = rememberNavController() // 상위에 선언하면 앱 죽음
                MyProfileScreenNavHost(
                    navController = profileNavController,
                    onSetting = { rootNavController.settings() },
                    onEmailLogin = { rootNavController.emailLogin() },
                    onReview = {
                        Log.d(TAG, "MyProfileScreen onReview reviewId : ${it}")
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
                    },
                    onMessage = onMessage
                )
            },
            findingMapScreen = {
                Finding(navController = rootNavController)
            },
            addReview = addReviewScreen,
            chat = chat
        )
    }
}