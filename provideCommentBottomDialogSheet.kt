package com.sarang.torang.di.main_di

import androidx.compose.runtime.Composable
import com.sarang.torang.RootNavController
import com.sarang.torang.di.comment_di.CommentBottomSheet
import com.sarang.torang.di.image.provideTorangAsyncImage

fun provideCommentBottomDialogSheet(rootNavController: RootNavController): @Composable (reviewId: Int?, onHidden: (() -> Unit)) -> Unit =
    { reviewId, onHidden ->
        CommentBottomSheet(
            reviewId = reviewId,
            onDismissRequest = onHidden,
            onHidden = {
                onHidden.invoke()
            },
            content = { },
            image = provideTorangAsyncImage(),
            onImage = {
                rootNavController.profile(it)
            },
            onName = {
                rootNavController.profile(it)
            }
        )
    }