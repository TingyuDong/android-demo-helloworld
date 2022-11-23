package com.thoughtworks.androidtrain

import android.app.Application
import com.thoughtworks.androidtrain.modules.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TweetApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TweetApplication)
            modules(appModules)
        }
    }
}