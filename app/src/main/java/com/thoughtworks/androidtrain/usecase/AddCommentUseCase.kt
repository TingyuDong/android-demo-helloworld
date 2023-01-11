package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.TweetApplication
import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.repository.CommentsRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

open class AddCommentUseCase(
    private val commentRepository: CommentsRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher,
    private val application: TweetApplication
) {
    suspend operator fun invoke(tweetId: Int, commentContent: String) = withContext(ioDispatcher) {
        commentRepository.addComment(
            tweetId = tweetId,
            comment = Comment(content = commentContent, sender = application.getUserInfo())
        )
    }
}