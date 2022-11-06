package com.thoughtworks.androidtrain.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sender")
class SenderPO(
    @PrimaryKey
    @ColumnInfo(name = "user_name")
    val userName: String,
    @ColumnInfo(name = "nick")
    val nick: String,
    @ColumnInfo(name = "avatar")
    val avatar: String
)