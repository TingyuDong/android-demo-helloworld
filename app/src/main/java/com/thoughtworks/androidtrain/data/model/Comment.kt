package com.thoughtworks.androidtrain.data.model

import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO

data class Comment(
    val content: String,
    val sender: SenderPO?
)
