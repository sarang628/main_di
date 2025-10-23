package com.sarang.torang.di.main_di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.sarang.torang.LocalRestaurantItemImageLoader
import com.sarang.torang.RestaurantItemUiState
import com.sarang.torang.RestaurantListBottomSheetViewModel
import com.sarang.torang.RestaurantListBottomSheet_
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
import com.sarang.torang.di.finding_di.FindState
import com.sarang.torang.di.finding_di.rememberFindState
import com.sarang.torang.di.pinchzoom.PinchZoomImageBox
import com.sarang.torang.di.pinchzoom.PinchZoomState
import com.sarang.torang.di.pinchzoom.d
import com.sarang.torang.di.pinchzoom.imageLoader
import com.sarang.torang.di.pinchzoom.isZooming
import com.sarang.torang.di.restaurant_list_bottom_sheet_di.CustomRestaurantItemImageLoader
import com.sarang.torang.viewmodel.FeedDialogsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun provideMainScreen(rootNavController    : RootNavController,
                      find                 : @Composable () -> Unit                     = {},
                      feedGrid             : @Composable () -> Unit                     = {},
                      profile              : @Composable () -> Unit                     = {},
                      addReview            : @Composable (onClose: () -> Unit) -> Unit  = {},
                      chat                 : @Composable () -> Unit                     = {},
                      alarm                : @Composable () -> Unit                     = {},
                      findState            : FindState                                  = rememberFindState(),
                      showLog              : Boolean                                    = false,
                      dialogsViewModel     : FeedDialogsViewModel                       = hiltViewModel(),
                      feedScreenState      : FeedScreenState                            = rememberFeedScreenState(),
                      mainScreenState      : MainScreenState                            = rememberMainScreenState(),
                      bottomSheetViewModel : RestaurantListBottomSheetViewModel         = hiltViewModel()
) : @Composable () ->Unit = {
    val tag                     : String                                = "__provideMainScreen"
    val coroutineScope          : CoroutineScope                        = rememberCoroutineScope()
    val context                 : Context                               = LocalContext.current
    var zoomState               : PinchZoomState?                       by remember { mutableStateOf<PinchZoomState?>(null) } // Data shared between a zoomed image and the rest of the list when zooming.
    val bottomSheetUiState      : List<RestaurantItemUiState>           by bottomSheetViewModel.uiState.collectAsState()

    val restaurantBottomSheet : @Composable ( @Composable () -> Unit ) -> Unit = {
        CompositionLocalProvider(LocalRestaurantItemImageLoader provides CustomRestaurantItemImageLoader) {
            RestaurantListBottomSheet_ (
                modifier                = Modifier,
                uiState                 = bottomSheetUiState,
                sheetPeekHeight         = 0.dp,
                scaffoldState           = findState.bottomSheetState,
                onClickRestaurantName   = { coroutineScope.launch { findState.bottomSheetState.bottomSheetState.partialExpand() } },
                content                 = { it() }
            )
        }
    }

    val feed : @Composable (onChat: () -> Unit) -> Unit = { onChat ->
        CompositionLocalProvider(
            LocalPullToRefreshLayoutType        provides customPullToRefresh,
            LocalBottomDetectingLazyColumnType  provides CustomBottomDetectingLazyColumnType,
            LocalFeedImageLoader                provides CustomFeedImageLoader(
                zoomState   = zoomState,
                showLog     = showLog,
                onZoomState = {
                                zoomState = it
                              },
            )
        ) {
            FeedScreenWithProfile(
                rootNavController   = rootNavController,
                feedNavController   = rememberNavController(),
                dialogsViewModel    = dialogsViewModel,
                feedScreenState     = feedScreenState,
                onChat              = onChat,
                onMessage           = { ChatActivity.go(context, it) },
                scrollEnabled       = zoomState?.isZooming != true,
                swipeAble           = zoomState?.isZooming != true,
                onPage              = {
                    if (it.swipeable)
                        mainScreenState.swipeDisableForMillis(coroutineScope = coroutineScope)
                },
            )
        }
    }

    val pinchZoomImageBox : @Composable (@Composable ()->Unit)->Unit = {
        PinchZoomImageBox(
            imageLoader     = imageLoader,
            activeZoomState = zoomState,
            showLog         = showLog,
            content         = it
        )
    }

    val mainScreen = @Composable {
        MainScreen(
            state           = mainScreenState,
            feed            = feed,
            feedGrid        = feedGrid,
            addReview       = addReview,
            find            = find,
            profile         = profile,
            chat            = chat,
            alarm           = alarm,
            swipeAble       = mainScreenState.isSwipeEnabled,
            onBottomMenu    = {
                //TODO 현재 피드화면에서 한 번 더 눌렀을 때 onTop 호출하기
                //coroutineScope.launch { feedScreenState.onTop() }
            }
        )
    }

    ProvideMainDialog(
        dialogsViewModel = dialogsViewModel,
        rootNavController = rootNavController,
        commentBottomSheet = provideCommentBottomDialogSheet(rootNavController),
        restaurantBottomSheet = restaurantBottomSheet
    ) {
        pinchZoomImageBox{
            mainScreen()
        }
    }
}

val FeedItemPageEvent.swipeable : Boolean get() = this.isLast || this.isFirst