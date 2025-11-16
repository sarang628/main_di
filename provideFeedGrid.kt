package com.sarang.torang.di.main_di

import com.sarang.torang.compose.type.FeedGridScreenType
import com.sarang.torang.di.feedgrid_di.ProvideTorangGrid

val provideFeedGridScreenType: FeedGridScreenType = {
    ProvideTorangGrid()
}

