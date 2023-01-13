package com.thoughtworks.androidtrain.data.source.local.room

import androidx.room.Embedded
import androidx.room.Relation
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO


data class CommentWithSender(
    @Embedded
    val commentPO: CommentPO,
    @Relation(
        parentColumn = "sender_name",
        entityColumn = "user_name"
    )
    val senderPO: SenderPO
)