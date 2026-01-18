package com.sarang.torang.di.main_di

import com.sarang.torang.RootNavController
import com.sarang.torang.compose.type.FindScreenType
import com.sarang.torang.di.finding_di.FindScreen
import com.sarang.torang.di.finding_di.FindState

fun provideFindScreenType(
    findState: FindState,
    rootNavController: RootNavController = RootNavController()
): FindScreenType = {
    FindScreen(
        findState = findState,
        navController = rootNavController
    ).invoke()
}