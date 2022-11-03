package com.thoughtworks.androidtrain.data.model

import com.thoughtworks.androidtrain.data.source.local.room.entity.Sender

data class Comment(
    val content: String,
    val sender: Sender?
)
