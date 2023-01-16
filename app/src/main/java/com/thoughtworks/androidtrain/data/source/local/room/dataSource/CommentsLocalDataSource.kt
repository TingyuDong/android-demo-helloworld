package com.thoughtworks.androidtrain.data.source.local.room.dataSource

import com.thoughtworks.androidtrain.data.source.local.room.dao.CommentDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO

class CommentsLocalDataSource(
    private val commentDao: CommentDao
) {
    suspend fun addComment(commentPO: CommentPO): Long {
        return commentDao.insertComment(commentPO)
    }

    suspend fun addAllComments(commentPOList: List<CommentPO>) {
        return commentDao.insertAllComments(commentPOList)
    }
}