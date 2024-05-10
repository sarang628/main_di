package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sarang.torang.compose.feed.Feed
import com.sarang.torang.compose.feed.MainFeedScreen
import com.sarang.torang.di.feed_di.review
import com.sarang.torang.di.image.provideTorangAsyncImage

fun provideFeedScreen(
    navController: NavHostController,
    feedNavController: NavHostController,
    progressTintColor: Color? = null,
    onImage: ((Int) -> Unit)? = null,
    onAddReview: () -> Unit,
    onShowComment: () -> Unit,
    currentBottomMenu: String,
    onConsumeCurrentBottomMenu: () -> Unit,
    profile: @Composable (NavBackStackEntry) -> Unit,
): @Composable (onComment: ((Int) -> Unit), onMenu: ((Int) -> Unit), onShare: ((Int) -> Unit), navBackStackEntry: NavBackStackEntry) -> Unit =
    { onComment, onMenu, onShare, navBackStackEntry ->
        var scrollEnabled by remember { mutableStateOf(true) }
        var onTop by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = currentBottomMenu) {
            if (currentBottomMenu == "feed") {

                if (feedNavController.currentDestination?.route != "mainFeed") {
                    feedNavController.popBackStack("mainFeed", inclusive = false)
                } else {
                    onTop = true
                }

                onConsumeCurrentBottomMenu.invoke()
            }
        }

        NavHost(navController = feedNavController, startDestination = "mainFeed") {
            composable("mainFeed") {
                MainFeedScreen(
                    onAddReview = onAddReview,
                    onTop = onTop,
                    consumeOnTop = { onTop = false },
                    feed = {
                        Feed(
                            review = it.review(
                                onComment = {
                                    onComment.invoke(it.reviewId)
                                    onShowComment.invoke()
                                },
                                onShare = { onShare.invoke(it.reviewId) },
                                onMenu = { onMenu.invoke(it.reviewId) },
                                onName = { feedNavController.navigate("profile/${it.userId}") },
                                onRestaurant = { navController.navigate("restaurant/${it.restaurantId}") },
                                onImage = onImage,
                                onProfile = { feedNavController.navigate("profile/${it.userId}") }
                            ),
                            isZooming = { scrollEnabled = !it },
                            progressTintColor = progressTintColor,
                            image = provideTorangAsyncImage()
                        )
                    }
                )
            }
            composable("profile/{id}") {
                profile.invoke(it)
            }
            composable("myFeed/{reviewId}") {
                ProvideMyFeedScreen(/*provideFeedScreen*/
                    navController = navController,
                    reviewId = it.arguments?.getString("reviewId")?.toInt() ?: 0,
                    onEdit = { navController.navigate("modReview/${it}") },
                    onProfile = { feedNavController.navigate("profile/${it}") },
                    onBack = { feedNavController.popBackStack() }
                )
            }
        }
    }
