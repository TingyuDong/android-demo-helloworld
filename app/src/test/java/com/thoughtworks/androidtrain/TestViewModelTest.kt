package com.thoughtworks.androidtrain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.rule.DispatchersRule
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import com.thoughtworks.androidtrain.utils.getOrAwaitValue
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TestViewModelTest {

    private lateinit var testViewModel: TestViewModel

    @Mock
    private lateinit var fetchTweetsUseCase: FetchTweetsUseCase

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val dispatchersRule = DispatchersRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun initViewModel() = runTest {
        testViewModel = TestViewModel(fetchTweetsUseCase = fetchTweetsUseCase)
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
        Mockito.`when`(fetchTweetsUseCase.invoke()).thenReturn(tweetList)
        //when
        testViewModel.setTweetFromLocal()
        //then
        assertEquals(tweetList, testViewModel.tweets.value)
        assertEquals(tweetList, testViewModel.tweets2.getOrAwaitValue())
    }
}