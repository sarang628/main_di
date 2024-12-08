package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.AlarmScreen

fun provideAlarm(rootNavController: RootNavController): @Composable () -> Unit = {
    AlarmScreen(
        onEmailLogin = { rootNavController.emailLogin() },
        onContents = { rootNavController.review(it) },
        onProfile = { rootNavController.profile(it) })
}