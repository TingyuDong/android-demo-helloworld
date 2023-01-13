package com.thoughtworks.androidtrain.data.source.local.room

import androidx.room.Embedded
import androidx.room.Relation
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import com.thoughtworks.androidtrain.data.source.local.room.entity.ImagePO
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO

data class TweetWithSenderAndCommentsAndImages(
    @Embedded
    val tweetPO: TweetPO,
    @Relation(
        parentColumn = "sender_name",
        entityColumn = "user_name"
    )
    val senderPO: SenderPO,
    @Relation(
        entity = CommentPO::class,
        parentColumn = "id",
        entityColumn = "tweet_id"
    )
    val commentPOList: List<CommentWithSender>?,
    @Relation(
        parentColumn = "id",
        entityColumn = "tweet_id"
    )
    val imagePOList: List<ImagePO>?
)