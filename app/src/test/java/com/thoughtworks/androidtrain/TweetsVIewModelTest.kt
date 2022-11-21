package com.thoughtworks.androidtrain

import android.app.Application
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class TweetsVIewModelTest {
    @Mock
    private lateinit var fetchTweetsUseCase: FetchTweetsUseCase
    @Mock
    private lateinit var addCommentUseCase: AddCommentUseCase
    @Mock
    private lateinit var addTweetUseCase: AddTweetUseCase
    @Mock
    private lateinit var application: Application
    private lateinit var fakeTweets: List<Tweet>

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

//    @Rule
//    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun beforeEach() {
        fetchTweetsUseCase = Mockito.mock(FetchTweetsUseCase::class.java)
        addCommentUseCase = Mockito.mock(AddCommentUseCase::class.java)
        addTweetUseCase = Mockito.mock(AddTweetUseCase::class.java)
        application = Mockito.mock(Application::class.java)
        fakeTweets = listOf()
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_return_null_list_if_both_remote_and_local_data_are_null() = runTest {
        //given
        `when`(fetchTweetsUseCase.invoke()).thenReturn(fakeTweets)
        val tweetsViewModel = TweetsViewModel(
            fetchTweetsUseCase = fetchTweetsUseCase,
            addCommentUseCase = addCommentUseCase,
            addTweetUseCase = addTweetUseCase,
            application = application
        )
        //when
        tweetsViewModel.fetchData()
        //then
        assertSame(tweetsViewModel.tweets.value,fakeTweets)
    }
}