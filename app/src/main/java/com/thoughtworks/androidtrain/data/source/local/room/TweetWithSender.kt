package com.thoughtworks.androidtrain.data.source.local.room

import androidx.room.Embedded
import androidx.room.Relation
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import com.thoughtworks.androidtrain.data.source.local.room.entity.ImagePO
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO


data class TweetWithSender(
    @Embedded(prefix = sender_prefix)
    val senderPO: SenderPO,
    @Embedded(prefix = tweet_prefix)
    val tweetPO: TweetPO,
    @Relation(
        entity = CommentPO::class,
        parentColumn = tweet_prefix+"id",
        entityColumn = "tweet_id"
    )
    val commentPOList: List<CommentWithSender>?,
    @Relation(
        parentColumn = tweet_prefix+"id",
        entityColumn = "tweet_id"
    )
    val imagePOList: List<ImagePO>?
){
    companion object {
        const val sender_prefix: String = "sender_"
        const val tweet_prefix: String = "tweet_"
    }
}