package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.source.local.room.dao.CommentDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import kotlinx.coroutines.flow.Flow

interface CommentRepositoryInterface {
    fun getCommentsStream(): Flow<List<CommentPO>>
    suspend fun getComments(tweetId: Int): List<Comment>?
    suspend fun addComments(comments: List<Comment>, tweetId: Int)
    fun addComment(commentPO: CommentPO)
}

class CommentRepository(
    private val commentDao: CommentDao,
    private val senderRepository: SenderRepository
) : CommentRepositoryInterface {
    override fun getCommentsStream(): Flow<List<CommentPO>> {
        return commentDao.observeComments()
    }

    override suspend fun getComments(tweetId: Int): List<Comment>? {
        val commentsPO = commentDao.getComments(tweetId)
        return commentsPO?.mapNotNull {
            transformToComment(it)
        }
    }

    override suspend fun addComments(comments: List<Comment>, tweetId: Int) {
        val commentsCollect = comments.map { comment ->
            senderRepository.addSender(comment.sender)
            CommentPO(
                id = 0,
                tweetId = tweetId,
                content = comment.content,
                senderName = comment.sender.username
            )
        }
        commentDao.insertAllComments(commentsCollect)
    }

    override fun addComment(commentPO: CommentPO) {
        commentDao.insertComment(commentPO)
    }

    private suspend fun transformToComment(commentPO: CommentPO): Comment? {
        return Comment(
            content = commentPO.content,
            sender = senderRepository.getSender(commentPO.senderName) ?: return null
        )
    }

}