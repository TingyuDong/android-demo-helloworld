package com.thoughtworks.androidtrain.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.thoughtworks.androidtrain.TweetApplication
import com.thoughtworks.androidtrain.data.repository.CommentsRepositoryImpl
import com.thoughtworks.androidtrain.data.repository.ImagesRepositoryImpl
import com.thoughtworks.androidtrain.data.repository.SendersRepositoryImpl
import com.thoughtworks.androidtrain.data.repository.TweetsRepositoryImpl
import com.thoughtworks.androidtrain.data.repository.CommentsRepository
import com.thoughtworks.androidtrain.data.repository.ImagesRepository
import com.thoughtworks.androidtrain.data.repository.SendersRepository
import com.thoughtworks.androidtrain.data.repository.TweetsRepository
import com.thoughtworks.androidtrain.tweets.TweetsViewModel
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import com.thoughtworks.androidtrain.data.source.local.room.CommentsLocalDataSource
import com.thoughtworks.androidtrain.data.source.local.room.ImagesLocalDataSource
import com.thoughtworks.androidtrain.data.source.local.room.SendersLocalDataSource
import com.thoughtworks.androidtrain.data.source.local.room.TweetsLocalDataSource
import com.thoughtworks.androidtrain.data.source.remote.TweetsRemoteDataSource
import com.thoughtworks.androidtrain.service.TweetService
import com.thoughtworks.androidtrain.service.TweetsApiHelperImpl
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val DATABASE_NAME = "tweet"
private const val BASE_URL = "https://thoughtworks-mobile-2018.herokuapp.com"
private const val PREFERENCE_FILE_KEY = "UserInfo"

val retrofitModules = module {
    single {
        GsonBuilder()
            .serializeNulls()
            .create()
    }

    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(get()))
            .baseUrl(BASE_URL)
            .build()
    }
}

val apiHelperModules = module {
    single {
        get<Retrofit>().create(TweetService::class.java)
    }

    single {
        TweetsApiHelperImpl(get())
    }
}

val daoModules = module {
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
}

val localDataModules = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    includes(daoModules)

    single {
        CommentsLocalDataSource(get())
    }

    single {
        ImagesLocalDataSource(get())
    }

    single {
        SendersLocalDataSource(get())
    }

    single {
        TweetsLocalDataSource(get())
    }
}

val remoteDataSourceModules = module {
    includes(
        retrofitModules,
        apiHelperModules,
    )

    single {
        TweetsRemoteDataSource(get(), get())
    }
}

val dataSourceModules = module {
    includes(localDataModules, remoteDataSourceModules)
}

val repositoryModules = module {
    single<CommentsRepository> {
        CommentsRepositoryImpl(get(), get())
    }

    single<ImagesRepository> {
        ImagesRepositoryImpl(get())
    }

    single<SendersRepository> {
        SendersRepositoryImpl(get())
    }

    single<TweetsRepository> {
        TweetsRepositoryImpl(get(), get())
    }
}

val useCaseModules = module {
    factory {
        AddCommentUseCase(get(), get(), get())
    }

    factory {
        AddTweetUseCase(get(), get(), get(), get(), get())
    }

    factory {
        FetchTweetsUseCase(get(), get(), get(), get(), get())
    }
}

val appModules = module {
    includes(
        dataSourceModules,
        repositoryModules,
        useCaseModules
    )

    single {
        Dispatchers.IO
    }

    single {
        TweetApplication()
    }

    single {
        get<Application>().getSharedPreferences(
            PREFERENCE_FILE_KEY,
            Context.MODE_PRIVATE
        )
    }

    viewModel {
        TweetsViewModel(get(), get(), get())
    }
}