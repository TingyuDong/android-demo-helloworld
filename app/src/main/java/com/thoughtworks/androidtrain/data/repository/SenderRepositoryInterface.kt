package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Sender

interface SenderRepositoryInterface {
    fun getSender(senderId: Int): Sender?
    fun addSender(sender: Sender?): Long?
}