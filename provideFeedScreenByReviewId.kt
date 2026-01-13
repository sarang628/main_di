package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.sarang.torang.dialogsbox.compose.DialogsBoxViewModel
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.FeedItem
import com.sarang.torang.compose.feed.FeedScreenByRestaurantId
import com.sarang.torang.compose.feed.FeedScreenByReviewId
import com.sarang.torang.compose.feed.internal.components.type.LocalExpandableTextType
import com.sarang.torang.compose.feed.internal.components.type.LocalFeedImageLoader
import com.sarang.torang.compose.feed.type.FeedTypeData
import com.sarang.torang.compose.feed.type.LocalFeedCompose
import com.sarang.torang.di.basefeed_di.CustomExpandableTextType
import com.sarang.torang.di.basefeed_di.CustomFeedImageLoader
import com.sarang.torang.di.dialogsbox_di.ProvideDialogsBox
import com.sarang.torang.di.feed_di.toReview
import com.sryang.library.pullrefresh.rememberPullToRefreshState

fun provideFeedScreenByReviewId(rootNavController: RootNavController): @Composable (Int) -> Unit =
    { reviewId ->
        val dialogsViewModel: DialogsBoxViewModel = hiltViewModel()
        val state = rememberPullToRefreshState()
        ProvideDialogsBox(
            rootNavController = rootNavController,
            dialogsViewModel = dialogsViewModel
        ) {
            CompositionLocalProvider(LocalFeedImageLoader provides CustomFeedImageLoader(),
                LocalFeedCompose provides { data : FeedTypeData ->
                    FeedItem(
                        uiState = data.feed.toReview(data.isLogin),
                        feedItemClickEvents = generateCommonFeedItemClickEvent( feedData = data,
                            dialogsViewModel = dialogsViewModel,
                            navController = rememberNavController(),
                            rootNavController = rootNavController ),
                    )
                },
                LocalExpandableTextType provides CustomExpandableTextType
            ) {
                FeedScreenByReviewId(
                    reviewId = reviewId,
                    onBack = { rootNavController.navController?.popBackStack() }
                )
            }
        }
    }