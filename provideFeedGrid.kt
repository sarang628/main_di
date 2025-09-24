package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import com.sarang.torang.di.feedgrid_di.ProvideTorangGrid

fun provideFeedGrid(): @Composable () -> Unit = {
    ProvideTorangGrid()
}