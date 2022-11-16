package com.thoughtworks.androidtrain

import android.app.Application
import com.thoughtworks.androidtrain.data.repository.DatabaseRepository
import okhttp3.OkHttpClient

class TweetApplication : Application() {
    private lateinit var client: OkHttpClient

    override fun onCreate() {
        super.onCreate()
        DatabaseRepository.initialize(this)
        client = OkHttpClient()
    }

    fun getHttpClient() : OkHttpClient {
        return client
    }
}