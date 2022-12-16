package com.thoughtworks.androidtrain.data.source.local.room

import com.thoughtworks.androidtrain.data.source.local.room.dao.TweetDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TweetsLocalDataSource(
    private val tweetDao: TweetDao,
) {
    fun getTweetsStream(): Flow<Result<List<TweetPO>>> {
        return tweetDao.observeTasks().map {
            Result.success(it)
        }
    }
}