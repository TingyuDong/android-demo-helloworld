package com.thoughtworks.androidtrain.data.source.remote

import com.google.gson.GsonBuilder
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.service.TweetService
import com.thoughtworks.androidtrain.utils.OkHttpUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class TweetsRemoteDataSource(
    private val ioDispatcher: CoroutineDispatcher,
    private val okHttpUtils: OkHttpUtils
) {
    suspend fun fetchRemoteTweets(): List<Tweet> {
        val gson = GsonBuilder()
            .serializeNulls()
            .create()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://thoughtworks-mobile-2018.herokuapp.com")
            .build()
        val service = retrofit.create(TweetService::class.java)
        return withContext(ioDispatcher) {
            try {
                val response = service.listTweets().execute()
                if (response.isSuccessful) {
                    return@withContext response.body()?:emptyList()
                } else {
                    return@withContext emptyList()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return@withContext emptyList()
            }

//            val url = "https://thoughtworks-mobile-2018.herokuapp.com/user/jsmith/tweets"
//            Gson().fromJson(
//                Objects.requireNonNull(okHttpUtils.getSync(url)),
//                object : TypeToken<ArrayList<Tweet>>() {}.type
//            )
        }

    }
}