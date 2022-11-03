package com.thoughtworks.androidtrain.data.repository

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.thoughtworks.androidtrain.TweetApplication
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import com.thoughtworks.androidtrain.data.source.local.room.entity.Tweet

private const val DATABASE_NAME = "tweet"

class Repository private constructor(context: Context) : TweetRepository {
    private val database: AppDatabase =
        Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
            .build()

    private val tweetDap = database.tweetDao()

    companion object {
        private var INSTANCE: Repository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = Repository(context)
            }
        }

        fun get(): Repository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }

    override fun fetchTweets(): List<Tweet> {
        return listOf()
    }
}