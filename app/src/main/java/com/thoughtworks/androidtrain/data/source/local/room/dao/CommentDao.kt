package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllComments(commentsPO: List<CommentPO>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(commentPO: CommentPO): Long
}