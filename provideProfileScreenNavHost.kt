package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.profile.LocalProfileImage
import com.sarang.torang.compose.profile.ProfileScreenNavHost
import com.sarang.torang.di.image.provideTorangAsyncImage

internal fun provideProfileScreenNavHost(
    rootNavController: RootNavController,
    onMessage: (Int) -> Unit = {}
): @Composable (NavBackStackEntry) -> Unit = { navBackStackEntry ->
    CompositionLocalProvider(
        LocalProfileImage provides {provideTorangAsyncImage().invoke(
            it.modifier,
            it.url,
            it.errorIconSize,
            it.progressSize,
            it.contentScale)}
    ) {
        ProfileScreenNavHost(
            id = navBackStackEntry.arguments?.getString("id")?.toInt(),
            onClose = { rootNavController.popBackStack() },
            onEmailLogin = { rootNavController.emailLogin() },
            onReview = { rootNavController.review(it) },
            onMessage = onMessage
        )
    }
}