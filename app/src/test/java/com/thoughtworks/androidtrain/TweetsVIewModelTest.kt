package com.thoughtworks.androidtrain

import android.app.Application
import android.support.test.runner.AndroidJUnit4
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.rule.DispatchersRule
import com.thoughtworks.androidtrain.tweets.TweetsViewModel
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import com.thoughtworks.androidtrain.utils.JSONResourceUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.annotation.Config
import kotlin.collections.ArrayList

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@Config(sdk = [29])
@RunWith(AndroidJUnit4::class)
class TweetsVIewModelTest {
    private lateinit var tweetsViewModel: TweetsViewModel

    @Mock
    private lateinit var fetchTweetsUseCase: FetchTweetsUseCase

    @Mock
    private lateinit var addCommentUseCase: AddCommentUseCase

    @Mock
    private lateinit var addTweetUseCase: AddTweetUseCase

    private lateinit var fakeTweets: List<Tweet>

    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    @get:Rule
    val dispatchersRule = DispatchersRule()

    @Before
    fun beforeEach() {
        fetchTweetsUseCase = Mockito.mock(FetchTweetsUseCase::class.java)
        addCommentUseCase = Mockito.mock(AddCommentUseCase::class.java)
        addTweetUseCase = Mockito.mock(AddTweetUseCase::class.java)
        tweetsViewModel = TweetsViewModel(
            fetchTweetsUseCase = fetchTweetsUseCase,
            addCommentUseCase = addCommentUseCase,
            addTweetUseCase = addTweetUseCase
        )
//        val resources = RuntimeEnvironment.application.resources
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_return_null_list_if_both_remote_and_local_data_are_null() = runTest {
        //given
        fakeTweets = listOf()

        `when`(fetchTweetsUseCase.invoke()).thenReturn(fakeTweets)
        //when
        tweetsViewModel.fetchData()
        //then
        assertSame(fakeTweets, tweetsViewModel.tweets.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_return_tweet_list_if_fetch_data() = runTest {
        //given
        val fakeTweets = Gson().fromJson<List<Tweet>>(
            JSONResourceUtils().jsonResourceReader(
                ApplicationProvider.getApplicationContext<Application>().resources, R.raw.tweets
            ),
            object : TypeToken<ArrayList<Tweet>>() {}.type
        )
        `when`(fetchTweetsUseCase.invoke()).thenReturn(fakeTweets)
        //when
        tweetsViewModel.fetchTweets()
        //then
        assertSame(fakeTweets, tweetsViewModel.tweets.value)
    }
}