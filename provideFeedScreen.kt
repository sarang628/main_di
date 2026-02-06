package com.sarang.torang.di.main_di

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.sarang.torang.dialogsbox.compose.DialogsBoxViewModel
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainScreenState
import com.sarang.torang.compose.feed.internal.components.type.LocalFeedImageLoader
import com.sarang.torang.compose.feed.state.FeedScreenState
import com.sarang.torang.compose.feed.type.LocalBottomDetectingLazyColumnType
import com.sarang.torang.compose.feed.type.LocalPullToRefreshLayoutType
import com.sarang.torang.compose.type.FeedScreenType
import com.sarang.torang.di.basefeed_di.CustomFeedImageLoader
import com.sarang.torang.di.chat_di.ChatActivity
import com.sarang.torang.di.feed_di.CustomBottomDetectingLazyColumnType
import com.sarang.torang.di.feed_di.customPullToRefresh
import com.sarang.torang.di.pinchzoom.PinchZoomState
import com.sarang.torang.di.pinchzoom.isZooming

fun provideLocalFeedScreenType(showLog           : Boolean = false,
                               zoomState         : PinchZoomState?,
                               onZoomState       : (PinchZoomState?)->Unit   = {},
                               dialogsViewModel  : DialogsBoxViewModel,
                               feedScreenState   : FeedScreenState,
                               rootNavController : RootNavController,
                               mainScreenState   : MainScreenState
): FeedScreenType = { onChat ->
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    CompositionLocalProvider(LocalPullToRefreshLayoutType        provides customPullToRefresh,
        LocalBottomDetectingLazyColumnType  provides CustomBottomDetectingLazyColumnType,
        LocalFeedImageLoader                provides CustomFeedImageLoader(zoomState   = zoomState,
                                                                           showLog     = showLog,
                                                                           onZoomState = onZoomState,)
    ) {
        FeedScreenWithProfile(rootNavController   = rootNavController,
                              feedNavController   = rememberNavController(),
                              dialogsViewModel    = dialogsViewModel,
                              feedScreenState     = feedScreenState,
                              onChat              = onChat,
                              onMessage           = { ChatActivity.go(context, it) },
                              scrollEnabled       = zoomState?.isZooming != true,
                              swipeAble           = zoomState?.isZooming != true,
                              onAlarm             = { rootNavController.goAlarm() },
                              onPage              = {
                                  if (it.swipeable)
                                      mainScreenState.swipeDisableForMillis(coroutineScope = coroutineScope)
                              },
                              videoPlayScrollVelocity = 5
        )
    }
}