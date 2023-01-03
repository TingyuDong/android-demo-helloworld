package com.thoughtworks.androidtrain.data.source.local.room

import com.thoughtworks.androidtrain.data.source.local.room.dao.CommentDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import kotlinx.coroutines.flow.Flow

class CommentsLocalDataSource(
    private val commentDao: CommentDao
) {
    fun getCommentStream(): Flow<List<CommentPO>> {
        return commentDao.observeComments()
    }

    suspend fun getCommentsByTweetsId(tweetId: Int): List<CommentPO>? {
        return commentDao.getCommentsByTweetId(tweetId)
    }

    suspend fun addComment(commentPO: CommentPO): Long {
        return commentDao.insertComment(commentPO)
    }

    suspend fun addAllComments(commentPOList: List<CommentPO>) {
        return commentDao.insertAllComments(commentPOList)
    }
}