package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.FeedScreenForMain
import com.sarang.torang.viewmodels.FeedDialogsViewModel

@Composable
fun FeedScreenWithProfile(
    rootNavController: RootNavController,
    dialogsViewModel: FeedDialogsViewModel,
) {
    val feedNavHostController = rememberNavController()
    NavHost(navController = feedNavHostController, startDestination = "feed") {
        composable("feed") {
            FeedScreenForMain(
                onAddReview = { rootNavController.addReview() },
                feed = provideFeed(
                    { dialogsViewModel.onComment(it) },
                    { dialogsViewModel.onMenu(it) },
                    { dialogsViewModel.onShare(it) },
                    navController = feedNavHostController,
                    rootNavController = rootNavController
                ),
                consumeOnTop = {},
                onTop = false
            )
        }
        composable(
            "profile/{id}",
            content = provideProfileScreen(
                rootNavController = rootNavController,
                navController = feedNavHostController
            )
        )
    }
}