package com.sarang.torang.di.main_di

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sarang.torang.RootNavController
import com.sarang.torang.di.comment_di.CommentBottomSheet
import com.sarang.torang.di.image.provideTorangAsyncImage
import kotlinx.coroutines.flow.Flow

fun provideCommentBottomDialogSheet(rootNavController: RootNavController): @Composable (reviewId: Int?, onHidden: (() -> Unit)) -> Unit =
    { reviewId, onHidden ->
        var currentReviewId: Int? by remember { mutableStateOf(null) }
        var show by remember { mutableStateOf(false) }

        if (currentReviewId != reviewId) {
            currentReviewId = reviewId;
            show = true
        }

        Log.d("__sryang", "show : ${show}")

        CommentBottomSheet(
            reviewId = reviewId,
            onDismissRequest = onHidden,
            onHidden = {
                onHidden.invoke()
                show = false
            },
            show = show,
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