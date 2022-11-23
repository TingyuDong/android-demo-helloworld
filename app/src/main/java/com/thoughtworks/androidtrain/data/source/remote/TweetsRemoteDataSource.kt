package com.thoughtworks.androidtrain.data.source.remote

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.utils.OkHttpUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class TweetsRemoteDataSource(
    private val ioDispatcher: CoroutineDispatcher,
    private val okHttpUtils: OkHttpUtils
) {
    suspend fun fetchRemoteTweets(): ArrayList<Tweet> =
        withContext(ioDispatcher) {
            val url = "https://thoughtworks-mobile-2018.herokuapp.com/user/jsmith/tweets"
            Gson().fromJson(
                Objects.requireNonNull(okHttpUtils.getSync(url)),
                object : TypeToken<ArrayList<Tweet>>() {}.type
            )
        }
}