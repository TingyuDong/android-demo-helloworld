package com.thoughtworks.androidtrain.service

import com.thoughtworks.androidtrain.data.model.Tweet
import retrofit2.Call
import retrofit2.http.GET

interface TweetService {
    @GET("/user/jsmith/tweets")
    fun listTweets(): Call<List<Tweet>>
}