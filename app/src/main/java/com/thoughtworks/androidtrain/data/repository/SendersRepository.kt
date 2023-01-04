package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.source.local.room.SendersLocalDataSource
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO
import kotlinx.coroutines.flow.Flow

interface SendersRepositoryInterface {
    fun getSendersStream(): Flow<List<SenderPO>>
    suspend fun getSender(userName: String): Sender?
    suspend fun addSender(sender: Sender): Long?
}

class SendersRepository(
    private val senderDataSource: SendersLocalDataSource
) : SendersRepositoryInterface {
    override fun getSendersStream(): Flow<List<SenderPO>> {
        return senderDataSource.getSendersStream()
    }

    override suspend fun getSender(userName: String): Sender? {
        return transformToSender(senderDataSource.getSender(userName))
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
        return senderDataSource.addSender(
            SenderPO(
                userName = sender.username,
                nick = sender.nick,
                avatar = sender.avatar
            )
        )
    }
}