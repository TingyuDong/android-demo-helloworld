package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.TweetApplication
import com.thoughtworks.androidtrain.data.repository.CommentsRepository
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

open class AddCommentUseCase(
    private val commentRepository: CommentsRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val application: TweetApplication
) {
    suspend operator fun invoke(tweetId: Int, commentContent: String) = withContext(ioDispatcher) {
        val commentPO = CommentPO(
                id = 0,
                tweetId = tweetId,
                content = commentContent,
                senderName = application.getUserInfo().username
            )
        commentRepository.addComment(commentPO)
    }
}