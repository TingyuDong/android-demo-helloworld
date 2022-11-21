package com.thoughtworks.androidtrain

import android.app.Application
import com.thoughtworks.androidtrain.data.repository.*
import com.thoughtworks.androidtrain.modules.appModules
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TweetApplication : Application() {
    private lateinit var client: OkHttpClient

    override fun onCreate() {
        super.onCreate()
        DatabaseRepository.initialize(this)
        DatabaseRepository.get().getDatabase()
        client = OkHttpClient()
        startKoin {
            androidContext(this@TweetApplication)
            modules(appModules)
        }
    }

    fun getHttpClient(): OkHttpClient {
        return client
    }
}