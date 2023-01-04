package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.source.local.room.CommentsLocalDataSource
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import kotlinx.coroutines.flow.Flow

interface CommentsRepositoryInterface {
    fun getCommentsStream(): Flow<List<CommentPO>>
    suspend fun getComments(tweetId: Int): List<Comment>?
    suspend fun addComments(comments: List<Comment>, tweetId: Int)
    suspend fun addComment(commentPO: CommentPO)
}

class CommentsRepository(
    private val commentDataSource: CommentsLocalDataSource,
    private val senderRepository: SendersRepository
) : CommentsRepositoryInterface {
    override fun getCommentsStream(): Flow<List<CommentPO>> {
        return commentDataSource.getCommentStream()
    }

    override suspend fun getComments(tweetId: Int): List<Comment>? {
        val commentsPO = commentDataSource.getCommentsByTweetsId(tweetId)
        return commentsPO?.mapNotNull {
            transformToComment(it)
        }
    }

    override suspend fun addComments(comments: List<Comment>, tweetId: Int) {
        commentDataSource.addAllComments(comments.map { comment ->
            senderRepository.addSender(comment.sender)
            transformToCommentPO(comment, tweetId)
        })
    }

    private fun transformToCommentPO(
        comment: Comment,
        tweetId: Int
    ): CommentPO {
        return CommentPO(
            id = 0,
            tweetId = tweetId,
            content = comment.content,
            senderName = comment.sender.username
        )
    }

    override suspend fun addComment(commentPO: CommentPO) {
        commentDataSource.addComment(commentPO)
    }

    private suspend fun transformToComment(commentPO: CommentPO): Comment? {
        return Comment(
            content = commentPO.content,
            sender = senderRepository.getSender(commentPO.senderName) ?: return null
        )
    }

}