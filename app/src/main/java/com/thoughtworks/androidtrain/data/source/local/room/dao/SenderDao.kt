package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.thoughtworks.androidtrain.data.source.local.room.entity.Sender

@Dao
interface SenderDao {
    @Query("SELECT * FROM sender where id =:senderId")
    fun getSender(senderId: Int): Sender?
}