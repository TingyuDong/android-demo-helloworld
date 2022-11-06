package com.thoughtworks.androidtrain.data.repository

import android.content.Context
import androidx.room.Room
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase

private const val DATABASE_NAME = "tweet"

interface DatabaseRepositoryInterface {
    fun getDatabase(): AppDatabase
}

class DatabaseRepository private constructor(context: Context) : DatabaseRepositoryInterface {
    private val database: AppDatabase =
        Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
            .allowMainThreadQueries()
            .build()

    companion object {
        private var INSTANCE: DatabaseRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = DatabaseRepository(context)
            }
        }

        fun get(): DatabaseRepository {
            return INSTANCE ?: throw IllegalStateException("DatabaseRepository must be initialized")
        }
    }

    override fun getDatabase(): AppDatabase {
        return database
    }
}