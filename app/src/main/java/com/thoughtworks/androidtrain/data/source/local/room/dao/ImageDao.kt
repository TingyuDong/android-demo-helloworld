package com.thoughtworks.androidtrain.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thoughtworks.androidtrain.data.source.local.room.entity.ImagePO
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Query("SELECT * FROM image")
    fun observeImages(): Flow<List<ImagePO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllImages(commentsPO: List<ImagePO>)
}