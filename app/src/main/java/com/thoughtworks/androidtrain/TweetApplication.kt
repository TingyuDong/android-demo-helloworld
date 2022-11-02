package com.thoughtworks.androidtrain

import android.app.Application
import androidx.room.Room
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase

class TweetApplication : Application() {
    private lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        initDatabase()
    }

    fun getDb() : AppDatabase {
        return db
    }

    private fun initDatabase() {
        db =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "tweet")
                .allowMainThreadQueries().build()
    }
}