package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.feed.FeedItem
import com.sarang.torang.compose.feed.FeedScreenByReviewId
import com.sarang.torang.compose.feed.internal.components.LocalExpandableTextType
import com.sarang.torang.compose.feed.internal.components.LocalFeedImageLoader
import com.sarang.torang.compose.feed.type.FeedTypeData
import com.sarang.torang.compose.feed.type.LocalFeedCompose
import com.sarang.torang.di.basefeed_di.CustomExpandableTextType
import com.sarang.torang.di.basefeed_di.CustomFeedImageLoader
import com.sarang.torang.di.dialogsbox_di.ProvideDialogsBox
import com.sarang.torang.di.feed_di.toReview
import com.sarang.torang.dialogsbox.compose.DialogsBoxViewModel

@Composable
fun ProvideMyFeedScreen(
    dialogsViewModel: DialogsBoxViewModel = hiltViewModel(),
    rootNavController: RootNavController,
    reviewId : Int
) {
    ProvideDialogsBox(
        dialogsViewModel = dialogsViewModel,
        rootNavController = rootNavController,
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