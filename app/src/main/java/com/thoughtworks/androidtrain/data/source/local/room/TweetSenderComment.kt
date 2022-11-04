package com.thoughtworks.androidtrain.data.source.local.room

import com.thoughtworks.androidtrain.data.source.local.room.entity.ImagePO

data class TweetSenderComment(
    val id: Int,
    val content: String?,
    val images: List<ImagePO>
)