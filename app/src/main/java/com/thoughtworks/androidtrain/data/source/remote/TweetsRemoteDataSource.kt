package com.thoughtworks.androidtrain.data.source.remote

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thoughtworks.androidtrain.data.model.Tweet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import kotlin.collections.ArrayList

class TweetsRemoteDataSource(
    private val ioDispatcher: CoroutineDispatcher,
    private val okHttpClient: OkHttpClient
) {
    suspend fun fetchRemoteTweets(): ArrayList<Tweet> =
    // Move the execution to an IO-optimized thread since the ApiService
        // doesn't support coroutines and makes synchronous requests.
        withContext(ioDispatcher) {
            val url = "https://thoughtworks-mobile-2018.herokuapp.com/user/jsmith/tweets"
            val response = okHttpClient.newCall(
                Request.Builder()
                    .url(url)
                    .build()
            ).execute()
            Gson().fromJson(
                Objects.requireNonNull(response.body?.string()),
                object : TypeToken<ArrayList<Tweet>>() {}.type
            )
        }
}