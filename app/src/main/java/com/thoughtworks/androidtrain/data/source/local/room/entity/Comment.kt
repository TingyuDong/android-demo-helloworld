package com.thoughtworks.androidtrain.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

class Comment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val commentId: Int,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "sender_id")
    val senderId: Int
) {
}