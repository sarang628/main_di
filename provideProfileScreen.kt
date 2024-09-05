package com.sarang.torang.di.main_di

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.ProfileScreenNavHost
import com.sarang.torang.di.image.provideTorangAsyncImage

internal fun provideProfileScreen(
    rootNavController: RootNavController,
    navController: NavHostController,
    videoPlayer: @Composable (url: String, isPlaying: Boolean, onVideoClick: () -> Unit) -> Unit,
): @Composable (NavBackStackEntry) -> Unit =
    {
        val profileNavController = rememberNavController() // 상위에 선언하면 앱 죽음
        val userId = it.arguments?.getString("id")?.toInt()
        if (userId != null) {
            ProfileScreenNavHost(
                navController = profileNavController,
                id = userId,
                onClose = { navController.popBackStack() },
                onEmailLogin = { rootNavController.emailLogin() },
                onReview = { profileNavController.navigate("myFeed/${it}") },
                myFeed = {
                    ProvideMyFeedScreen(
                        navBackStackEntry = it,
                        rootNavController = rootNavController,
                        navController = profileNavController,
                        videoPlayer = videoPlayer
                    )
                },
                image = provideTorangAsyncImage()
            )
        } else {
            Text(text = "사용자 정보가 없습니다.")
        }
    }
