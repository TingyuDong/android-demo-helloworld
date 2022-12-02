package com.thoughtworks.androidtrain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.utils.getOrAwaitValue
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TestViewModelTest {

    private lateinit var testViewModel: TestViewModel

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initViewModel() {
        testViewModel = TestViewModel()
    }

    @Test
    fun isLiveDataEmitting_observeForever() {
        val tweetList = listOf(
            Tweet(1, null, null, null, null, null, null),
            Tweet(2, null, null, null, null, null, null),
            Tweet(3, null, null, null, null, null, null)
        )
        testViewModel.setNewTweet(tweetList)
        assertEquals(testViewModel.tweets.value, tweetList)
        assertEquals(testViewModel.tweets2.getOrAwaitValue(), tweetList)
    }
}