package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.source.local.room.dao.SenderDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO
import kotlinx.coroutines.flow.Flow

interface SenderRepositoryInterface {
    fun getSendersStream(): Flow<List<SenderPO>>
    suspend fun getSender(userName: String): Sender?
    suspend fun addSender(sender: Sender): Long?
}

class SenderRepository(private val senderDao: SenderDao) : SenderRepositoryInterface {
    override fun getSendersStream(): Flow<List<SenderPO>> {
        return senderDao.observeSenders()
    }

    override suspend fun getSender(userName: String): Sender? {
        return transformToSender(senderDao.getSender(userName))
    }

    private fun transformToSender(senderPO: SenderPO?): Sender? {
        senderPO ?: return null
        return Sender(
            username = senderPO.userName,
            nick = senderPO.nick,
            avatar = senderPO.avatar
        )
    }

    override suspend fun addSender(sender: Sender): Long {
        return senderDao.insertSender(
            SenderPO(
                userName = sender.username,
                nick = sender.nick,
                avatar = sender.avatar
            )
        )
    }
}