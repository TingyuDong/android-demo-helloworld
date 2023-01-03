package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO
import kotlinx.coroutines.flow.Flow

@Dao
interface SenderDao {
    @Query("SELECT * FROM sender")
    fun observeSenders(): Flow<List<SenderPO>>

    @Query("SELECT * FROM sender WHERE user_name =:userName")
    suspend fun getSender(userName: String): SenderPO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSender(sender: SenderPO): Long
}