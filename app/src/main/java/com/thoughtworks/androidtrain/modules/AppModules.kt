package com.thoughtworks.androidtrain.modules

import androidx.room.Room
import com.google.gson.GsonBuilder
import com.thoughtworks.androidtrain.TweetApplication
import com.thoughtworks.androidtrain.tweets.TweetsViewModel
import com.thoughtworks.androidtrain.data.repository.CommentRepository
import com.thoughtworks.androidtrain.data.repository.ImageRepository
import com.thoughtworks.androidtrain.data.repository.SenderRepository
import com.thoughtworks.androidtrain.data.repository.TweetRepository
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import com.thoughtworks.androidtrain.data.source.local.room.TweetsLocalDataSource
import com.thoughtworks.androidtrain.data.source.remote.TweetsRemoteDataSource
import com.thoughtworks.androidtrain.service.TweetService
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import com.thoughtworks.androidtrain.utils.OkHttpUtils
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val DATABASE_NAME = "tweet"
private const val BASE_URL = "https://thoughtworks-mobile-2018.herokuapp.com"

val appModules = module {
    single {
        GsonConverterFactory.create(
            GsonBuilder()
                .serializeNulls()
                .create()
        )
    }
    single {
        Retrofit.Builder()
            .addConverterFactory(get())
            .baseUrl(BASE_URL)
            .build()
    }
    single {
        get<Retrofit>().create(TweetService::class.java)
    }
    single {
        TweetsRemoteDataSource(get(), get())
    }
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
        TweetsLocalDataSource(get())
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
        TweetRepository(get(), get(), get())
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
        FetchTweetsUseCase(get(), get(), get(), get())
    }
    single {
        TweetApplication()
    }
    viewModel {
        TweetsViewModel(get(), get(), get())
    }
}