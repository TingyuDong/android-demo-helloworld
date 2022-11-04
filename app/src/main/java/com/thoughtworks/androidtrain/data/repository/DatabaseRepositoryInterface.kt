package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase

interface DatabaseRepositoryInterface {
    fun getDatabase(): AppDatabase
}