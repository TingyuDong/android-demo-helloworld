package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.thoughtworks.androidtrain.data.source.local.room.entity.Tweet

@Dao
interface TweetDao {
    @Query("SELECT * FROM tweets")
    fun getAll(): List<Tweet>
}