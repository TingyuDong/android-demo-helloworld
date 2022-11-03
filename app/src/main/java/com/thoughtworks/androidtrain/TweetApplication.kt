package com.thoughtworks.androidtrain

import android.app.Application
import androidx.room.Room
import com.thoughtworks.androidtrain.data.repository.Repository
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase

class TweetApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.initialize(this)
    }
}