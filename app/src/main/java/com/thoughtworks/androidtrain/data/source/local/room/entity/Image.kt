package com.thoughtworks.androidtrain.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Image(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val imageId: Int,
    @ColumnInfo(name = "url")
    val url: String
) {
}