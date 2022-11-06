package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.source.local.room.dao.SenderDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO

interface SenderRepositoryInterface {
    fun getSender(userName: String): Sender?
    fun addSender(sender: Sender): Long?
}

class SenderRepository(private val senderDao: SenderDao) : SenderRepositoryInterface {

    override fun getSender(userName: String): Sender? {
        val senderPO = senderDao.getSender(userName)
        if (senderPO != null) {
            return Sender(senderPO.userName, senderPO.nick, senderPO.avatar)
        }
        return null
    }

    override fun addSender(sender: Sender): Long {
        val senderPO = SenderPO(sender.username, sender.nick, sender.avatar)
        return senderDao.insertSender(senderPO)
    }

}