package com.thoughtworks.androidtrain.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tweets")
data class TweetPO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "content")
    val content: String?,
    @ColumnInfo(name = "sender_name")
    val senderName: String,
    @ColumnInfo(name = "error")
    val error: String?,
    @ColumnInfo(name = "unknown_error")
    val unknownError: String?
)