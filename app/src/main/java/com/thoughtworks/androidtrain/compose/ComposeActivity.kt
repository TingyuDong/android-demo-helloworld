package com.thoughtworks.androidtrain.compose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.thoughtworks.androidtrain.TweetApplication
import com.thoughtworks.androidtrain.TweetsViewModel
import com.thoughtworks.androidtrain.data.repository.*
import com.thoughtworks.androidtrain.data.source.remote.TweetsRemoteDataSource
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.Dispatchers

//// Inject Library
//fun provideUseCase(useCase: UserCase) {
//    //
//}
//fun getUserCase(): UseCase {
//    //
//}

class ComposeActivity : AppCompatActivity() {
    private lateinit var tweetsViewModel: TweetsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        setContent {
            TweetScreen(
                tweetsViewModel = tweetsViewModel
            )
        }
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