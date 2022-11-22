package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.repository.*
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

open class AddCommentUseCase(
    private val senderRepository: SenderRepository,
    private val commentRepository: CommentRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(comment: Comment, tweetId: Int) = withContext(ioDispatcher) {
        val commentPO = comment.sender.let { sender ->
            senderRepository.addSender(sender)
            CommentPO(
                id = 0,
                tweetId = tweetId,
                content = comment.content,
                senderName = sender.username
            )
        }
        commentRepository.addComment(commentPO)
    }
}