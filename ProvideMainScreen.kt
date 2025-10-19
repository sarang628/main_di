package com.sarang.torang.di.main_di

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.sarang.torang.compose.feed.type.LocalBottomDetectingLazyColumnType
import com.sarang.torang.compose.feed.type.LocalPullToRefreshLayoutType
import com.sarang.torang.data.basefeed.FeedItemPageEvent
import com.sarang.torang.di.basefeed_di.CustomFeedImageLoader
import com.sarang.torang.di.chat_di.ChatActivity
import com.sarang.torang.di.feed_di.CustomBottomDetectingLazyColumnType
import com.sarang.torang.di.feed_di.customPullToRefresh
import com.sarang.torang.di.finding_di.FindState
import com.sarang.torang.di.pinchzoom.PinchZoomImageBox
import com.sarang.torang.di.pinchzoom.imageLoader
import com.sarang.torang.di.restaurant_list_bottom_sheet_di.CustomRestaurantItemImageLoader
import com.sarang.torang.viewmodel.FeedDialogsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun provideFeedScreen(
    navController           : RootNavController,
    dialogsViewModel        : FeedDialogsViewModel,
    feedScreenState         : FeedScreenState,
    mainScreenState         : MainScreenState
) : @Composable (onChat: () -> Unit) -> Unit = { onChat ->
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    CompositionLocalProvider(
        LocalFeedImageLoader                provides CustomFeedImageLoader,
        LocalPullToRefreshLayoutType        provides customPullToRefresh,
        LocalBottomDetectingLazyColumnType  provides CustomBottomDetectingLazyColumnType
    ) {
        FeedScreenWithProfile(
            rootNavController   = navController,
            feedNavController   = rememberNavController(),
            dialogsViewModel    = dialogsViewModel,
            feedScreenState     = feedScreenState,
            onChat              = onChat,
            onMessage           = { ChatActivity.go(context, it) },
            onPage              = {
                if (it.swipeable)
                    mainScreenState.swipeDisableForMillis(coroutineScope = coroutineScope)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun provideMainScreen(
    navController   : RootNavController,
    dialogsViewModel: FeedDialogsViewModel,
    feedScreenState : FeedScreenState,
    mainScreenState : MainScreenState,
    findState       : FindState,
    find            : @Composable () -> Unit = {},
    feedGrid        : @Composable () -> Unit = {},
    profile         : @Composable () -> Unit = {},
    addReview       : @Composable (onClose: () -> Unit) -> Unit = {},
    chat            : @Composable () -> Unit = {},
    alarm           : @Composable () -> Unit = {},
    feed            : @Composable (onChat: () -> Unit) -> Unit = {}
) : @Composable () ->Unit = {
    val tag = "__provideMainScreen"
    val coroutineScope          : CoroutineScope                        = rememberCoroutineScope()
    val bottomSheetViewModel    : RestaurantListBottomSheetViewModel    = hiltViewModel()
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

    val onBottomMenu: (Any) -> Unit = {
        if (mainScreenState.isFeedOnTop(it)) {
            coroutineScope.launch { feedScreenState.onTop() }
        }
    }

    ProvideMainDialog(
        dialogsViewModel        = dialogsViewModel,
        rootNavController       = navController,
        commentBottomSheet      = provideCommentBottomDialogSheet(navController),
        restaurantBottomSheet   = restaurantBottomSheet
    ) {
        PinchZoomImageBox(imageLoader = imageLoader) {
            MainScreen(
                feedGrid            = feedGrid,
                state               = mainScreenState,
                myProfileScreen     = profile,
                findingMapScreen    = find,
                addReview           = addReview,
                chat                = chat,
                alarm               = alarm,
                swipeAble           = mainScreenState.isSwipeEnabled,
                feedScreen          = feed,
                onBottomMenu        = onBottomMenu
            )
        }
    }
}

val FeedItemPageEvent.swipeable : Boolean get() = this.isLast || this.isFirst