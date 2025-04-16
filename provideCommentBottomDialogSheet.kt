package com.sarang.torang.di.main_di

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sarang.torang.RootNavController
import com.sarang.torang.di.comment_di.CommentBottomSheet
import com.sarang.torang.di.image.provideTorangAsyncImage

fun provideCommentBottomDialogSheet(
    rootNavController: RootNavController,
): @Composable (reviewId: Int?, onHidden: () -> Unit, content: @Composable (PaddingValues) -> Unit) -> Unit =
    { reviewId, onHidden, content ->
        var currentReviewId: Int? by remember { mutableStateOf(null) }
        var show by remember { mutableStateOf(false) }

        Log.d("__sryang", reviewId.toString());

        if (currentReviewId != reviewId) {
            currentReviewId = reviewId;
            show = true
        }

        CommentBottomSheet(
            reviewId = reviewId,
            onDismissRequest = onHidden,
            onHidden = { onHidden.invoke(); show = false },
            show = show,
            content = content,
            image = { a, b, c, d, e ->
                provideTorangAsyncImage()
                    .invoke(a, b, c, d, e, null)
            },
            onImage = { rootNavController.profile(it) },
            onName = { rootNavController.profile(it) }
        )
    }