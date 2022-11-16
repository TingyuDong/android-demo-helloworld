package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.source.local.room.dao.TweetDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO

interface TweetRepositoryInterface {
    fun getAllTweets(): List<TweetPO>
    fun addTweet(tweetPO: TweetPO): Long
}

class TweetRepository(private val tweetDao: TweetDao) : TweetRepositoryInterface {

    override fun getAllTweets(): List<TweetPO> {
        return tweetDao.getAll()
    }

    override fun addTweet(tweetPO: TweetPO): Long {
        return tweetDao.insertTweet(tweetPO)
    }
}