package com.thoughtworks.androidtrain.usecase

import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.repository.*
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddCommentUseCase {
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    private val database: AppDatabase = DatabaseRepository.get().getDatabase()
    private val senderDao = database.senderDao()
    private val commentDao = database.commentDao()

    private val senderRepository = SenderRepository(senderDao)
    private val commentRepository = CommentRepository(commentDao, senderDao)

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