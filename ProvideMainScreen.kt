package com.sarang.torang.di.main_di

import android.content.Context
import android.os.Build
import android.util.Log
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sarang.torang.LocalRestaurantItemImageLoader
import com.sarang.torang.RestaurantItemUiState
import com.sarang.torang.RestaurantListBottomSheetViewModel
import com.sarang.torang.RestaurantListBottomSheet_
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainScreen
import com.sarang.torang.compose.MainScreenState
import com.sarang.torang.compose.feed.state.FeedScreenState
import com.sarang.torang.compose.feed.state.rememberFeedScreenState
import com.sarang.torang.compose.rememberMainScreenState
import com.sarang.torang.compose.type.AlarmScreenType
import com.sarang.torang.compose.type.ChatScreenType
import com.sarang.torang.compose.type.FindScreenType
import com.sarang.torang.compose.type.LocalAlarmScreenType
import com.sarang.torang.compose.type.LocalChatScreenType
import com.sarang.torang.compose.type.LocalFeedGridScreenType
import com.sarang.torang.compose.type.LocalFeedScreenType
import com.sarang.torang.compose.type.LocalFindScreenType
import com.sarang.torang.compose.type.LocalMyProfileScreenType
import com.sarang.torang.compose.type.MyProfileScreenType
import com.sarang.torang.data.basefeed.FeedItemPageEvent
import com.sarang.torang.di.chat_di.provideChatScreen
import com.sarang.torang.di.finding_di.FindState
import com.sarang.torang.di.finding_di.rememberFindState
import com.sarang.torang.di.pinchzoom.PinchZoomImageBox
import com.sarang.torang.di.pinchzoom.PinchZoomState
import com.sarang.torang.di.pinchzoom.imageLoader
import com.sarang.torang.di.profile_di.provideMyProfileScreenNavHost
import com.sarang.torang.di.profile_di.provideProfileScreen
import com.sarang.torang.di.restaurant_list_bottom_sheet_di.CustomRestaurantItemImageLoader
import com.sarang.torang.viewmodel.FeedDialogsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun provideMainScreen(
    rootNavController    : RootNavController,
    findState            : FindState                                  = rememberFindState(),
    showLog              : Boolean                                    = false,
    dialogsViewModel     : FeedDialogsViewModel                       = hiltViewModel(),
    feedScreenState      : FeedScreenState                            = rememberFeedScreenState(),
    mainScreenState      : MainScreenState                            = rememberMainScreenState(),
    bottomSheetViewModel : RestaurantListBottomSheetViewModel         = hiltViewModel(),
    findScreen           : FindScreenType                             = {},
    alarmScreen          : AlarmScreenType                            = {},
    chatScreen           : ChatScreenType                             = {},
    myProfileScreen      : MyProfileScreenType                        = {},
    bottomNavBarHeight   : Dp                                         = 80.dp,
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

    val pinchZoomImageBox : @Composable (@Composable ()->Unit)->Unit = {
        PinchZoomImageBox(
            imageLoader     = imageLoader,
            activeZoomState = zoomState,
            showLog         = showLog,
            content         = it
        )
    }

    val mainScreen = @Composable {
        CompositionLocalProvider(
            LocalFindScreenType provides findScreen,
            LocalAlarmScreenType provides alarmScreen,
            LocalFeedGridScreenType provides provideFeedGridScreenType,
            LocalChatScreenType provides chatScreen,
            LocalMyProfileScreenType provides myProfileScreen,
            LocalFeedScreenType provides provideLocalFeedScreenType(
                zoomState   = zoomState,
                showLog     = showLog,
                onZoomState = { zoomState = it },
                rootNavController   = rootNavController,
                dialogsViewModel    = dialogsViewModel,
                feedScreenState = feedScreenState,
                mainScreenState = mainScreenState
            )
        ) {
            MainScreen(
                state               = mainScreenState,
                swipeAble           = mainScreenState.isSwipeEnabled,
                bottomNavBarHeight  = bottomNavBarHeight,
                onAlreadyFeed       = {
                    Log.d(tag, "onAlreadyFeed")
                    coroutineScope.launch { feedScreenState.onTop() }
                }
            )
        }
    }

    ProvideMainDialog(
        dialogsViewModel = dialogsViewModel,
        rootNavController = rootNavController,
        restaurantBottomSheet = restaurantBottomSheet
    ) {
        pinchZoomImageBox{
            mainScreen()
        }
    }
}

val FeedItemPageEvent.swipeable : Boolean get() = this.isLast || this.isFirst