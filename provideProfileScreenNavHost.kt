package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.ProfileScreenNavHost
import com.sarang.torang.di.image.provideTorangAsyncImage

internal fun provideProfileScreenNavHost(
    feedNavController: NavHostController,
    rootNavController: RootNavController,
    onMessage: (Int) -> Unit
): @Composable (NavBackStackEntry) -> Unit = { navBackStackEntry ->
    ProfileScreenNavHost(
        id = navBackStackEntry.arguments?.getString("id")?.toInt(),
        onClose = { feedNavController.popBackStack() },
        onEmailLogin = { rootNavController.emailLogin() },
        onReview = { feedNavController.navigate("myFeed/${it}") },
        myFeed = {
            ProvideMyFeedScreen(rootNavController = rootNavController,
                                navController = feedNavController,
                                navBackStackEntry = it,)
        },
        image = provideTorangAsyncImage(),
        onMessage = onMessage
    )
}