package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO

@Dao
interface SenderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSender(sender: SenderPO): Long
}