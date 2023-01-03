package com.thoughtworks.androidtrain.data.source.local.room

import com.thoughtworks.androidtrain.data.source.local.room.dao.SenderDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO
import kotlinx.coroutines.flow.Flow

class SendersLocalDataSource(
    private val senderDao: SenderDao
) {
    fun getSendersStream(): Flow<List<SenderPO>> {
        return senderDao.observeSenders()
    }

    suspend fun getSender(userName: String): SenderPO? {
        return senderDao.getSender(userName)
    }

    suspend fun addSender(senderPO: SenderPO): Long {
        return senderDao.insertSender(senderPO)
    }
}