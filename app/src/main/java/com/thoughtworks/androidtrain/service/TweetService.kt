package com.thoughtworks.androidtrain.service

import com.thoughtworks.androidtrain.data.model.Tweet
import retrofit2.http.GET

interface TweetService {
    @GET("/user/jsmith/tweets")
    suspend fun listTweets(): List<Tweet>
}