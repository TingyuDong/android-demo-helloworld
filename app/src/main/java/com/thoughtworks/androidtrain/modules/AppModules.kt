package com.thoughtworks.androidtrain.modules

import androidx.room.Room
import com.thoughtworks.androidtrain.TweetApplication
import com.thoughtworks.androidtrain.TweetsViewModel
import com.thoughtworks.androidtrain.data.repository.CommentRepository
import com.thoughtworks.androidtrain.data.repository.ImageRepository
import com.thoughtworks.androidtrain.data.repository.SenderRepository
import com.thoughtworks.androidtrain.data.repository.TweetRepository
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import com.thoughtworks.androidtrain.data.source.remote.TweetsRemoteDataSource
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import com.thoughtworks.androidtrain.utils.OkHttpUtils
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val DATABASE_NAME = "tweet"


val appModules = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
    single {
        OkHttpUtils(get())
    }
    single {
        TweetsRemoteDataSource(get(), get())
    }
    single {
        get<AppDatabase>().tweetDao()
    }
    single {
        get<AppDatabase>().commentDao()
    }
    single {
        get<AppDatabase>().imageDao()
    }
    single {
        get<AppDatabase>().senderDao()
    }
    single {
        SenderRepository(get())
    }
    single {
        CommentRepository(get(), get())
    }
    single {
        TweetRepository(get(), get())
    }
    single {
        ImageRepository(get())
    }
    single {
        Dispatchers.IO
    }
    single {
        AddCommentUseCase(get(), get(), get())
    }
    single {
        AddTweetUseCase(get(), get(), get(), get(), get())
    }
    single {
        FetchTweetsUseCase(get(), get(), get(), get(), get())
    }
    single {
        TweetApplication()
    }
    viewModel {
        TweetsViewModel(get(), get(), get(), get())
    }
}