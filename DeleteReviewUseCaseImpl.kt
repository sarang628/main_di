package com.sarang.torang.di.main_di

import com.sarang.torang.usecase.DeleteReviewUseCase
import com.sryang.torang_repository.repository.FeedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DeleteReviewUseCaseImpl {
    @Provides
    fun providesDeleteReviewUseCase(
        feedRepository: FeedRepository
    ): DeleteReviewUseCase {
        return object : DeleteReviewUseCase {
            override suspend fun invoke(reviewId : Int) {
                feedRepository.deleteFeed(reviewId)
            }
        }
    }
}