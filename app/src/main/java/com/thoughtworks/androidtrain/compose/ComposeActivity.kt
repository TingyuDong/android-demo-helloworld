package com.thoughtworks.androidtrain.compose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thoughtworks.androidtrain.R
import com.thoughtworks.androidtrain.TweetApplication
import com.thoughtworks.androidtrain.TweetsViewModel
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.*
import com.thoughtworks.androidtrain.data.source.remote.TweetsRemoteDataSource
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import com.thoughtworks.androidtrain.utils.JSONResourceUtils
import kotlinx.coroutines.Dispatchers

//// Inject Library
//fun provideUseCase(useCase: UserCase) {
//    //
//}
//fun getUserCase(): UseCase {
//    //
//}

class ComposeActivity : AppCompatActivity() {
    private var tweets = ArrayList<Tweet>()
    private lateinit var tweetsViewModel: TweetsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        tweets.addAll(
            Gson().fromJson(
                JSONResourceUtils().jsonResourceReader(resources, R.raw.tweets),
                object : TypeToken<ArrayList<Tweet>>() {}.type
            )
        )
        setContent {
//            TweetScreen((application as TweetApplication).getHttpClient())
            TweetScreen(
                tweetsViewModel = tweetsViewModel
            )
        }
    }

    @Preview
    @Composable
    fun ContentPreview() {
        tweets.add(getTweet())
        tweets.add(getTweet())
    }

    private fun getTweet(): Tweet {
        val sender = Sender(
            username = "Aiolia",
            nick = "Aio",
            avatar = "https://thoughtworks-mobile-2018.herokuapp.com/images/user/avatar/001.jpeg"
        )
        return Tweet(
            id = 7,
            content = "沙发",
            sender = sender,
            images = null,
            comments = null,
            error = null,
            unknownError = null
        )
    }

    private fun initViewModel() {
        val database = DatabaseRepository.get().getDatabase()
        val client = (application as TweetApplication).getHttpClient()
        val tweetsRemoteDataSource = TweetsRemoteDataSource(Dispatchers.Default, client)
        val senderRepository = SenderRepository(database.senderDao())
        val commentRepository = CommentRepository(database.commentDao(), database.senderDao())
        val tweetRepository = TweetRepository(database.tweetDao(), tweetsRemoteDataSource)
        val imageRepository = ImageRepository(database.imageDao())
        tweetsViewModel = TweetsViewModel(
            fetchTweetsUseCase = FetchTweetsUseCase(
                senderRepository = senderRepository,
                commentRepository = commentRepository,
                tweetRepository = tweetRepository,
                imageRepository = imageRepository
            ),
            addTweetUseCase = AddTweetUseCase(
                senderRepository = senderRepository,
                commentRepository = commentRepository,
                imageRepository = imageRepository,
                tweetRepository = tweetRepository
            ),
            addCommentUseCase = AddCommentUseCase(
                senderRepository = senderRepository,
                commentRepository = commentRepository
            ),
            application = application
        )
    }
}