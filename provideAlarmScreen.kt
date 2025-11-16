package com.sarang.torang.di.main_di

import com.sarang.torang.RootNavController
import com.sarang.torang.compose.AlarmScreen
import com.sarang.torang.compose.type.AlarmScreenType

fun provideAlarmScreen(rootNavController: RootNavController): AlarmScreenType = {
    AlarmScreen(
        onEmailLogin = { rootNavController.emailLogin() },
        onContents = { rootNavController.review(it) },
        onProfile = { rootNavController.profile(it) })
}