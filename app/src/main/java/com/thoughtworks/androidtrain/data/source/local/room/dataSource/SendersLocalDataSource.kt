package com.thoughtworks.androidtrain.data.source.local.room.dataSource

import com.thoughtworks.androidtrain.data.source.local.room.dao.SenderDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO

class SendersLocalDataSource(
    private val senderDao: SenderDao
) {
    suspend fun addSender(senderPO: SenderPO): Long {
        return senderDao.insertSender(senderPO)
    }
}