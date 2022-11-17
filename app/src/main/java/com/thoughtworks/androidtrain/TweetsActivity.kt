package com.thoughtworks.androidtrain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thoughtworks.androidtrain.adapters.TweetsAdapter
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.*
import com.thoughtworks.androidtrain.data.source.remote.TweetsRemoteDataSource
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.Dispatchers

class TweetsActivity : AppCompatActivity() {
    private lateinit var tweetsAdapter: TweetsAdapter
    private val tweets = ArrayList<Tweet?>()
    private lateinit var tweetViewModel: TweetsViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet)
        initViewModel()
        initUI()
        tweetViewModel.fetchData()
    }

    private fun initViewModel() {
        tweetViewModel = ViewModelProvider(this)[TweetsViewModel::class.java]
        val database = DatabaseRepository.get().getDatabase()
        val tweetsRemoteDataSource = TweetsRemoteDataSource(
            Dispatchers.Default,
            (application as TweetApplication).getHttpClient()
        )
        val senderRepository = SenderRepository(database.senderDao())
        val commentRepository = CommentRepository(database.commentDao(), database.senderDao())
        val tweetRepository = TweetRepository(database.tweetDao(), tweetsRemoteDataSource)
        val imageRepository = ImageRepository(database.imageDao())
        tweetViewModel = TweetsViewModel(
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
        tweetViewModel.tweetsData.observe(this) {
            tweets.addAll(it)
            addEmptyData()
            tweetsAdapter.notifyDataSetChanged()
        }
    }

    private fun initUI() {
        recyclerView = findViewById(R.id.tweet_recycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tweetsAdapter = TweetsAdapter(tweets)
        recyclerView.adapter = tweetsAdapter
    }

    private fun addEmptyData() {
        tweets.add(null)
    }
}