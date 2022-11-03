package com.thoughtworks.androidtrain.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Sender(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val senderId: Int,
    @ColumnInfo(name = "user_name")
    val userName: String,
    @ColumnInfo(name = "nick")
    val nick: String,
    @ColumnInfo(name = "avatar")
    val avatar: String
) {
}