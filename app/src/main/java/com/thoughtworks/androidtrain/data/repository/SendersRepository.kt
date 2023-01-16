package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.source.local.room.dataSource.SendersLocalDataSource
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO

interface SendersRepository {
    fun transformToSender(senderPO: SenderPO?): Sender?
    suspend fun addSender(sender: Sender): Long?
}

class SendersRepositoryImpl(
    private val senderDataSource: SendersLocalDataSource
) : SendersRepository {
    override fun transformToSender(senderPO: SenderPO?): Sender? {
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