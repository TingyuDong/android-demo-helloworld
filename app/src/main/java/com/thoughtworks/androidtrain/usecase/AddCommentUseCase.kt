package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.repository.*
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddCommentUseCase(
    private val senderRepository: SenderRepository,
    private val commentRepository: CommentRepository
) {
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    suspend operator fun invoke(comment: Comment, tweetId: Int) =
        withContext(defaultDispatcher) {
            val commentPO = comment.sender!!.let { sender ->
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