package com.thoughtworks.androidtrain.data.source

import app.cash.turbine.test
import com.thoughtworks.androidtrain.data.Result
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.data.repository.TweetsRepository
import com.thoughtworks.androidtrain.data.repository.TweetsRepositoryImpl
import com.thoughtworks.androidtrain.data.source.local.room.TweetWithSenderAndCommentsAndImages
import com.thoughtworks.androidtrain.data.source.local.room.dataSource.TweetsLocalDataSource
import com.thoughtworks.androidtrain.data.source.local.room.entity.SenderPO
import com.thoughtworks.androidtrain.data.source.local.room.entity.TweetPO
import com.thoughtworks.androidtrain.data.source.remote.TweetsRemoteDataSource
import com.thoughtworks.androidtrain.rule.DispatchersRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class TweetsRepositoryImplTest {
    private val remoteTweet1 = Tweet(0, "hello1", null, null, null, null, null)
    private val remoteTweet2 = Tweet(1, "hello2", null, null, null, null, null)
    private val newRemoteTweet = Tweet(2, "I'm new tweet", null, null, null, null, null)

    private val localTweet1 = TweetWithSenderAndCommentsAndImages(
        tweetPO = TweetPO(
            id = 0,
            content = "hello3",
            senderName = "Aiolia",
            error = null,
            unknownError = null
        ),
        senderPO = SenderPO(
            userName = "Aiolia",
            nick = "Aio",
            avatar = "https://developer.android.com/avatar/0231.jpg"
        ),
        commentPOList = null,
        imagePOList = null
    )
    private val newLocalTweet = TweetWithSenderAndCommentsAndImages(
        tweetPO = TweetPO(
            id = 1,
            content = "I'm new tweet",
            senderName = "Aiolia",
            error = null,
            unknownError = null
        ),
        senderPO = SenderPO(
            userName = "Aiolia",
            nick = "Aio",
            avatar = "https://developer.android.com/avatar/0231.jpg"
        ),
        commentPOList = null,
        imagePOList = null
    )

    private val remoteTweets = listOf(remoteTweet1, remoteTweet2).sortedBy { it.id }
    private val localTweets = listOf(localTweet1).sortedBy { it.tweetPO.id }
    private val newRemoteTweets = listOf(newRemoteTweet).sortedBy { it.id }
    private val newLocalTweets = listOf(localTweet1, newLocalTweet).sortedBy { it.tweetPO.id }
    private var remoteTweetsStream: MutableStateFlow<Result<List<Tweet>>> =
        MutableStateFlow(Result.Loading)
    private var _localTweetsStream: MutableStateFlow<List<TweetWithSenderAndCommentsAndImages>> =
        MutableStateFlow(localTweets)
    private var localTweetsStream: Flow<List<TweetWithSenderAndCommentsAndImages>> =
        _localTweetsStream

    private lateinit var tweetsRemoteDataSource: TweetsRemoteDataSource
    private lateinit var tweetsLocalDataSource: TweetsLocalDataSource

    private lateinit var tweetsRepository: TweetsRepository

    @get:Rule
    val dispatchersRule = DispatchersRule()

    @Before
    fun createRepository() {
        tweetsRemoteDataSource = Mockito.mock(TweetsRemoteDataSource::class.java)

        tweetsLocalDataSource = Mockito.mock(TweetsLocalDataSource::class.java)

        tweetsRepository = TweetsRepositoryImpl(tweetsRemoteDataSource, tweetsLocalDataSource)
    }

    @Test
    fun getRemoteTweetsStream_emptyRepositoryAndUninitializedCache() = runTest {
        //Given
        `when`(tweetsRemoteDataSource.getTweetsStream()).thenReturn(remoteTweetsStream)

        //When
        val remoteTweetsStream = tweetsRepository.getRemoteTweetsStream()

        //Then
        remoteTweetsStream.test {
            Assert.assertEquals(true, awaitItem() is Result.Loading)
        }
    }

    @Test
    fun getRemoteTweetsStream_repositoryCachesBeforeRefresh() = runTest {
        //Given
        `when`(tweetsRemoteDataSource.getTweetsStream()).thenReturn(remoteTweetsStream)
        remoteTweetsStream.update { Result.Success(remoteTweets) }

        //When
        val remoteTweetsStream = tweetsRepository.getRemoteTweetsStream()

        //Then
        // remoteTweetStream would reserve old remoteTweetStream
        remoteTweetsStream.test {
            Assert.assertEquals(Result.Success(remoteTweets), awaitItem())
        }
    }

    @Test
    fun getRemoteTweetsStream_repositoryCachesAfterRefresh() = runTest {
        //Given
        `when`(tweetsRemoteDataSource.refreshTweets()).then {
            remoteTweetsStream.update {
                Result.Success(newRemoteTweets)
            }
        }
        `when`(tweetsRemoteDataSource.getTweetsStream()).thenReturn(remoteTweetsStream)

        //When
        tweetsRepository.refreshTweets()
        val remoteTweetsStream = tweetsRepository.getRemoteTweetsStream()

        //Then
        remoteTweetsStream.test {
            Assert.assertEquals(Result.Success(newRemoteTweets), awaitItem())
        }
    }

    @Test
    fun getLocalTweetsStream() = runTest {
        //Given
        `when`(tweetsLocalDataSource.getTweetsStream()).thenReturn(localTweetsStream)

        //When
        val localTweetsStream = tweetsRepository.getLocalTweetsStream()

        //Then
        localTweetsStream.test {
            Assert.assertEquals(localTweets, awaitItem())
        }
    }

    @Test
    fun getLocalTweetsStream_afterDatabaseUpdate() = runTest {
        //Given
        `when`(tweetsLocalDataSource.getTweetsStream()).thenReturn(localTweetsStream)

        //When
        _localTweetsStream.update {
            newLocalTweets
        }
        val localTweetsStream = tweetsRepository.getLocalTweetsStream()

        //Then
        localTweetsStream.test {
            Assert.assertEquals(newLocalTweets, awaitItem())
        }
    }
}