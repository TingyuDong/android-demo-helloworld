package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO

@Dao
interface SenderDao {
    @Query("SELECT * FROM sender where id =:senderId")
    fun getSender(senderId: Int): SenderPO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSender(sender: SenderPO)
}