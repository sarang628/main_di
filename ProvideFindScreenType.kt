package com.sarang.torang.di.main_di

import com.sarang.torang.compose.type.FindScreenType
import com.sarang.torang.di.finding_di.FindState
import com.sarang.torang.di.finding_di.findingWithPermission

fun provideFindScreenType(findState: FindState): FindScreenType = {
    findingWithPermission(findState = findState).invoke()
}