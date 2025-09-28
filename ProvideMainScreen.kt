package com.sarang.torang.di.main_di

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.MainScreen
import com.sarang.torang.compose.feed.internal.components.LocalFeedImageLoader
import com.sarang.torang.compose.feed.state.FeedScreenState
import com.sarang.torang.compose.feed.state.rememberFeedScreenState
import com.sarang.torang.compose.main.Feed
import com.sarang.torang.di.chat_di.ChatActivity
import com.sarang.torang.di.image.provideImageLoader
import com.sarang.torang.di.pinchzoom.PinchZoomImageBox
import com.sarang.torang.di.pinchzoom.isZooming
import com.sarang.torang.viewmodels.FeedDialogsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    val dialogsViewModel: FeedDialogsViewModel = hiltViewModel()
    val feedScreenState : FeedScreenState  = rememberFeedScreenState()
    val feedNavController = rememberNavController() // 메인 하단 홈버튼 클릭시 처리를 위해 여기에 설정
    var latestDestination: Any by remember { mutableStateOf(Feed) }
    var goAlarm by remember { mutableStateOf(false) } // 알림화면 이동 플래그
    var isSwipeEnabled by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var job: Job? by remember { mutableStateOf(null) }

    LaunchedEffect("") {
        job = launch { isSwipeEnabled = false; delay(5000); isSwipeEnabled = true }
    }

    LaunchedEffect(latestDestination) {
        snapshotFlow { latestDestination }.collect {
            Log.d("__ProvideMainScreen", "latestDestination : $latestDestination")
        }
    }

    ProvideMainDialog(
        dialogsViewModel = dialogsViewModel,
        rootNavController = rootNavController,
        commentBottomSheet = provideCommentBottomDialogSheet(rootNavController)
    ) {
        PinchZoomImageBox(imageLoader = provideImageLoader()) { zoomableImage, zoomState ->
            MainScreen(
                swipeAblePager      = isSwipeEnabled && !zoomState.isZooming,
                feedGrid            = feedGrid,
                myProfileScreen     = myProfileScreen,
                findingMapScreen    = findingMapScreen,
                addReview           = addReview,
                chat                = chat,
                goAlarm             = goAlarm,
                alarm               = alarm,
                consumeAlarm        = { goAlarm = false },
                feedScreen = { onAddReview ->
                    CompositionLocalProvider(LocalFeedImageLoader provides { data -> zoomableImage.invoke(data.modifier, data.url ?:"" , data.contentScale, data.height) }) {
                        FeedScreenWithProfile(
                            rootNavController = rootNavController,
                            feedNavController = feedNavController,
                            dialogsViewModel = dialogsViewModel,
                            feedScreenState = feedScreenState,
                            onAddReview = onAddReview,
                            onAlarm = { goAlarm = true },
                            onMessage = { ChatActivity.go(context, it) },
                            onPage = { page, isFirst, isLast ->
                                job?.cancel() // 기존 Job이 실행 중이라면 취소
                                job = coroutineScope.launch { // 새로운 Job 실행
                                    if (isFirst || isLast) { isSwipeEnabled = false; delay(2000); isSwipeEnabled = true }
                                }
                            },
                            scrollEnabled = !zoomState.isZooming,
                            pageScrollable = !zoomState.isZooming
                        )
                    }
                },
                onBottomMenu = {
                    if (feedNavController.currentDestination?.route != "feed" && latestDestination == "feed" && it == "feed") {
                        feedNavController.popBackStack("feed", inclusive = false) // 피드 화면안에서 다른화면 상태일 때 피드 버튼을 눌렀다면 피드 화면으로 이동
                    } else if (latestDestination == "feed" && it == "feed") {
                        coroutineScope.launch {
                            feedScreenState.onTop() // 피드 화면 에서 피드 버튼을 눌렀을 때 리스트 최상단 이동
                        }
                    }
                    latestDestination = it
                }
            )
        }
    }
}