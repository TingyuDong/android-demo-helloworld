package com.thoughtworks.androidtrain.service

import com.thoughtworks.androidtrain.data.model.Tweet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface TweetsApiHelper {
    fun getTweets(): Flow<List<Tweet>>
}

class TweetsApiHelperImpl(private val tweetsService: TweetService) : TweetsApiHelper{
    override fun getTweets(): Flow<List<Tweet>> = flow {
        emit(tweetsService.listTweets())
    }
}