package com.sarang.torang.di.main_di

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pinchzoom.library.pinchzoom.rememberPinchZoomState
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainScreenState
import com.sarang.torang.compose.feed.FeedItem
import com.sarang.torang.compose.feed.FeedScreenInMain
import com.sarang.torang.compose.feed.internal.components.LocalExpandableTextType
import com.sarang.torang.compose.feed.internal.components.LocalFeedImageLoader
import com.sarang.torang.compose.feed.state.FeedScreenState
import com.sarang.torang.compose.feed.type.FeedTypeData
import com.sarang.torang.compose.feed.type.LocalBottomDetectingLazyColumnType
import com.sarang.torang.compose.feed.type.LocalFeedCompose
import com.sarang.torang.compose.feed.type.LocalPullToRefreshLayoutType
import com.sarang.torang.di.basefeed_di.CustomExpandableTextType
import com.sarang.torang.di.basefeed_di.CustomFeedImageLoader
import com.sarang.torang.di.chat_di.ChatActivity
import com.sarang.torang.di.feed_di.CustomBottomDetectingLazyColumnType
import com.sarang.torang.di.feed_di.customPullToRefresh
import com.sarang.torang.di.feed_di.toReview
import com.sarang.torang.viewmodel.FeedDialogsViewModel

fun provideFeedMainScreen(
    navController           : RootNavController,
    dialogsViewModel        : FeedDialogsViewModel,
    feedScreenState         : FeedScreenState,
    mainScreenState         : MainScreenState
) : @Composable (onChat: () -> Unit) -> Unit = { onChat ->
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val feedNavController = rememberNavController()

    NavHost(
        navController       = feedNavController,
        startDestination    = "feed") {
        composable("feed") {
            CompositionLocalProvider(
                LocalFeedCompose provides { data : FeedTypeData ->
                    FeedItem(
                        uiState             = data.feed.toReview(data.isLogin),
                        feedItemClickEvents = generateCommonFeedItemClickEvent(data, dialogsViewModel, feedNavController, navController),
                        onPage              = {
                            if (it.swipeable)
                                mainScreenState.swipeDisableForMillis(coroutineScope = coroutineScope)
                        },
                        pageScrollAble      = data.pageScrollable
                    )
                },
                LocalBottomDetectingLazyColumnType provides CustomBottomDetectingLazyColumnType,
                LocalPullToRefreshLayoutType provides customPullToRefresh,
                LocalFeedImageLoader provides CustomFeedImageLoader(rememberPinchZoomState()),
                LocalExpandableTextType provides CustomExpandableTextType
            ){
                FeedScreenInMain(
                    onAddReview         = onChat,
                    feedScreenState     = feedScreenState,
                    onAlarm             = {  },
                    scrollEnabled       = true,
                    pageScrollable      = true,
                    contentWindowInsets = WindowInsets(0.dp)
                )
            }
        }
        composable("profile/{id}"){
            ProvideProfileScreen(
                rootNavController   = navController,
                navController       = feedNavController,
                onMessage           = { ChatActivity.go(context, it) },
                navBackStackEntry   = it)
        }
    }
}