package com.thoughtworks.androidtrain.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tweets")
class TweetPO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val tweetId: Int,
    @ColumnInfo(name="content")
    val content: String?,
    @ColumnInfo(name="sender_id")
    val senderId: Int?,
    @ColumnInfo(name="error")
    val error: String?,
    @ColumnInfo(name="unknown_error")
    val unknownError: String?
    ) {
}