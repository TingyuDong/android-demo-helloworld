package com.thoughtworks.androidtrain.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
class CommentPO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "tweet_id")
    val TweetId: Int,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "sender_name")
    val senderName: String
)