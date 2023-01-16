package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.source.local.room.dataSource.CommentsLocalDataSource
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO

interface CommentsRepository {
    suspend fun addComments(comments: List<Comment>, tweetId: Int)
    suspend fun addComment(tweetId: Int, comment: Comment)
}

class CommentsRepositoryImpl(
    private val commentDataSource: CommentsLocalDataSource,
    private val senderRepository: SendersRepository
) : CommentsRepository {
    override suspend fun addComments(comments: List<Comment>, tweetId: Int) {
        commentDataSource.addAllComments(comments.map { comment ->
            senderRepository.addSender(comment.sender)
            transformToCommentPO(comment, tweetId)
        })
    }

    override suspend fun addComment(tweetId: Int, comment: Comment) {
        commentDataSource.addComment(transformToCommentPO(tweetId = tweetId, comment = comment))
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
}