package com.thoughtworks.androidtrain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import com.thoughtworks.androidtrain.utils.getOrAwaitValue
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

@RunWith(MockitoJUnitRunner::class)
class TestViewModelTest {

    private lateinit var testViewModel: TestViewModel

    @Mock
    private lateinit var fetchTweetsUseCase: FetchTweetsUseCase

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun initViewModel()  = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        testViewModel = TestViewModel(fetchTweetsUseCase = fetchTweetsUseCase, dispatcher)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun isLiveDataEmitting_observeForever() {
        val tweetList = listOf(
            Tweet(1, null, null, null, null, null, null),
            Tweet(2, null, null, null, null, null, null),
            Tweet(3, null, null, null, null, null, null),
        )
        testViewModel.setNewTweet(tweetList)
        assertEquals(testViewModel.tweets.value, tweetList)
        assertEquals(testViewModel.tweets2.getOrAwaitValue(), tweetList)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun isLiveDataEmitting_observeForever_with_mock_parameter() = runTest {
        //given
        val tweetList = listOf(
            Tweet(1, null, null, null, null, null, null),
            Tweet(2, null, null, null, null, null, null),
            Tweet(3, null, null, null, null, null, null),
        )
        fetchTweetsUseCase.stub {
            onBlocking { invoke() }.doReturn(tweetList)
        }
//        Mockito.`when`(fetchTweetsUseCase.fetchTweets()).thenReturn(tweetList)
        //when
        testViewModel.setTweetFromLocal()
        //then
        delay(5000)
        assertEquals(tweetList, testViewModel.tweets.value)
        assertEquals(tweetList, testViewModel.tweets2.getOrAwaitValue())
    }
}