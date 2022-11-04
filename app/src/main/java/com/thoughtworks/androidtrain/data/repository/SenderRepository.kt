package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO

class SenderRepository(repository: Repository): SenderRepositoryInterface {
    private val database = repository.getDatabase()

    private val senderDao = database.senderDao()

    override fun getSender(senderId: Int): Sender? {
        val senderPO = senderDao.getSender(senderId)
        if (senderPO != null) {
            return Sender(senderId, senderPO.userName, senderPO.nick, senderPO.avatar)
        }
        return null
    }

    override fun addSender(sender: Sender?): Long? {
        if (sender != null) {
            val senderPO = if (sender.id==null){
                SenderPO(0, sender.username, sender.nick, sender.avatar)
            }else{
                SenderPO(sender.id!!, sender.username, sender.nick, sender.avatar)
            }
            return senderDao.insertSender(senderPO)
        }
        return null
    }

}