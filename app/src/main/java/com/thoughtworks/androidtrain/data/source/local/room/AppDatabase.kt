package com.thoughtworks.androidtrain.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.thoughtworks.androidtrain.data.source.local.room.dao.CommentDao
import com.thoughtworks.androidtrain.data.source.local.room.dao.ImageDao
import com.thoughtworks.androidtrain.data.source.local.room.dao.SenderDao
import com.thoughtworks.androidtrain.data.source.local.room.dao.TweetDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.Comment
import com.thoughtworks.androidtrain.data.source.local.room.entity.Image
import com.thoughtworks.androidtrain.data.source.local.room.entity.Sender
import com.thoughtworks.androidtrain.data.source.local.room.entity.Tweet

@Database(entities = [Tweet::class, Comment::class, Image::class, Sender::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tweetDao(): TweetDao
    abstract fun commentDao(): CommentDao
    abstract fun imageDao(): ImageDao
    abstract fun senderDao(): SenderDao
}