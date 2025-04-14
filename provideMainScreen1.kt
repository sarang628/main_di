package com.sarang.torang.di.main_di

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.sarang.torang.RootNavController
import com.sarang.torang.di.addreview_di.provideAddReviewScreen
import com.sarang.torang.di.chat_di.ChatActivity
import com.sarang.torang.di.chat_di.provideChatScreen
import com.sarang.torang.di.video.provideVideoPlayer
import com.sryang.library.pullrefresh.PullToRefreshLayoutState
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun provideMainScreen1(
    rootNavController: RootNavController,
    state: PullToRefreshLayoutState
): @Composable () -> Unit {
    return {
        val context = LocalContext.current
        val coroutine = rememberCoroutineScope()
        provideMainScreen(
            rootNavController,
            videoPlayer = provideVideoPlayer(),
            addReviewScreen = provideAddReviewScreen(rootNavController),
            chat = provideChatScreen(state),
            onMessage = {
                coroutine.launch {
                    context.startActivity(Intent(context, ChatActivity::class.java).apply {
                        putExtra("roomId", it)
                    })
                }
            }).invoke()
    }
}