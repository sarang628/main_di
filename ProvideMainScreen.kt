package com.sarang.torang.di.main_di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainScreen
import com.sarang.torang.compose.MainScreenState
import com.sarang.torang.compose.feed.internal.components.LocalFeedImageLoader
import com.sarang.torang.compose.feed.state.FeedScreenState
import com.sarang.torang.compose.feed.state.rememberFeedScreenState
import com.sarang.torang.compose.rememberMainScreenState
import com.sarang.torang.di.basefeed_di.CustomFeedImageLoader
import com.sarang.torang.di.chat_di.ChatActivity
import com.sarang.torang.di.image.provideImageLoader
import com.sarang.torang.di.pinchzoom.PinchZoomImageBox
import com.sarang.torang.di.pinchzoom.isZooming
import com.sarang.torang.viewmodels.FeedDialogsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun provideMainScreen(rootNavController: RootNavController,
  findingMapScreen  : @Composable () -> Unit = {},
  feedGrid          : @Composable () -> Unit = {},
  myProfileScreen   : @Composable () -> Unit = {},
  addReview         : @Composable (onClose: () -> Unit) -> Unit = {},
  chat              : @Composable () -> Unit = {},
  alarm             : @Composable () -> Unit = {}
) : @Composable () ->Unit = {
    val dialogsViewModel: FeedDialogsViewModel  = hiltViewModel()
    val feedScreenState : FeedScreenState       = rememberFeedScreenState()
    val mainScreenState : MainScreenState       = rememberMainScreenState()
    val coroutineScope  : CoroutineScope        = rememberCoroutineScope()
    val context         : Context               = LocalContext.current

    ProvideMainDialog(
        dialogsViewModel = dialogsViewModel,
        rootNavController = rootNavController,
        commentBottomSheet = provideCommentBottomDialogSheet(rootNavController)
    ) {
        PinchZoomImageBox(imageLoader = provideImageLoader()) { zoomableImage ->
            MainScreen(
                swipeAblePager      = mainScreenState.isSwipeEnabled /*&& !zoomState.isZooming*/,
                state               = mainScreenState,
                feedGrid            = feedGrid,
                myProfileScreen     = myProfileScreen,
                findingMapScreen    = findingMapScreen,
                addReview           = addReview,
                chat                = chat,
                alarm               = alarm,
                feedScreen = { onChat ->
                    CompositionLocalProvider(LocalFeedImageLoader provides CustomFeedImageLoader) {
                        FeedScreenWithProfile(
                            rootNavController   = rootNavController,
                            feedNavController   = mainScreenState.feedNavController,
                            dialogsViewModel    = dialogsViewModel,
                            feedScreenState     = feedScreenState,
                            onChat              = onChat,
                            onAlarm             = { mainScreenState.goAlarm() },
                            onMessage           = { ChatActivity.go(context, it) },
                            scrollEnabled       = true/*!zoomState.isZooming*/,
                            pageScrollable      = true/*!zoomState.isZooming*/,
                            onPage = { page, isFirst, isLast ->
                                /*if (isFirst || isLast) mainScreenState.swipeDisableForMillies(coroutineScope = coroutineScope)*/
                            }
                        )
                    }
                },
                onBottomMenu = {
                    if (mainScreenState.isFeedPage && mainScreenState.latestDestination == "feed" && it == "feed") {
                        mainScreenState.popToFeed()
                    } else if (mainScreenState.latestDestination == "feed" && it == "feed") {
                        coroutineScope.launch {
                            feedScreenState.onTop() // 피드 화면 에서 피드 버튼을 눌렀을 때 리스트 최상단 이동
                        }
                    }
                    mainScreenState.latestDestination = it
                }
            )
        }
    }
}