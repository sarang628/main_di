package com.sarang.torang.di.main_di

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import com.sarang.torang.RootNavController
import com.sarang.torang.compose.AlarmScreen
import com.sarang.torang.compose.type.AlarmScreenType

@OptIn(ExperimentalMaterial3Api::class)
fun provideAlarmScreen(rootNavController: RootNavController): AlarmScreenType = {
    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = { IconButton({rootNavController.popBackStack()})
                                            {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                                    contentDescription =  null
                                                )
                                            }
                                       },
        title = { Text("알림") })
    })
    {
        Box(Modifier.padding(it)){
            AlarmScreen(
                onEmailLogin = { rootNavController.emailLogin() },
                onContents = { rootNavController.review(it) },
                onProfile = { rootNavController.profile(it) })
        }
    }
}