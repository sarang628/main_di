package com.sarang.torang.di.main_di

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainScreen
import com.sarang.torang.compose.MainScreenState
import com.sarang.torang.compose.feed.internal.components.LocalFeedImageLoader
import com.sarang.torang.compose.feed.state.FeedScreenState
import com.sarang.torang.compose.feed.state.rememberFeedScreenState
import com.sarang.torang.compose.feed.type.LocalBottomDetectingLazyColumnType
import com.sarang.torang.compose.feed.type.LocalPullToRefreshLayoutType
import com.sarang.torang.compose.rememberMainScreenState
import com.sarang.torang.data.basefeed.FeedItemPageEvent
import com.sarang.torang.di.basefeed_di.CustomFeedImageLoader
import com.sarang.torang.di.chat_di.ChatActivity
import com.sarang.torang.di.feed_di.CustomBottomDetectingLazyColumnType
import com.sarang.torang.di.feed_di.customPullToRefresh
import com.sarang.torang.di.pinchzoom.PinchZoomImageBox
import com.sarang.torang.viewmodel.FeedDialogsViewModel
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
    val tag = "__provideMainScreen"
    val dialogsViewModel: FeedDialogsViewModel = hiltViewModel()
    val feedScreenState: FeedScreenState = rememberFeedScreenState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    val mainScreenState: MainScreenState = rememberMainScreenState()

    Log.d(tag, "recomposition")

    ProvideMainDialog(
        dialogsViewModel = dialogsViewModel,
        rootNavController = rootNavController,
        commentBottomSheet = provideCommentBottomDialogSheet(rootNavController)
    ) {
        PinchZoomImageBox {
            MainScreen(
                feedGrid = feedGrid,
                state = mainScreenState,
                myProfileScreen = myProfileScreen,
                findingMapScreen = findingMapScreen,
                addReview = addReview,
                chat = chat,
                alarm = alarm,
                swipeAble = mainScreenState.isSwipeEnabled,
                feedScreen = { onChat ->
                    CompositionLocalProvider(
                        LocalFeedImageLoader provides CustomFeedImageLoader,
                        LocalPullToRefreshLayoutType provides customPullToRefresh,
                        LocalBottomDetectingLazyColumnType provides CustomBottomDetectingLazyColumnType
                    ) {
                        FeedScreenWithProfile(
                            rootNavController = rootNavController,
                            feedNavController = rememberNavController(),
                            dialogsViewModel = dialogsViewModel,
                            feedScreenState = feedScreenState,
                            onChat = onChat,
                            onMessage = { ChatActivity.go(context, it) },
                            onPage = {
                                Log.d(tag, it.swipeable.toString())
                                if (it.swipeable)
                                    mainScreenState.swipeDisableForMillis(coroutineScope = coroutineScope)
                            }
                        )
                    }
                },
                onBottomMenu = {
                    if (mainScreenState.isFeedOnTop(it)) {
                        coroutineScope.launch { feedScreenState.onTop() }
                    }
                }
            )
        }
    }
}

val FeedItemPageEvent.swipeable : Boolean get() = this.isLast || this.isFirst