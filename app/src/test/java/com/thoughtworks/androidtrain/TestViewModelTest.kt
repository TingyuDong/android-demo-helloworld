package com.thoughtworks.androidtrain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.rule.DispatchersRule
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import com.thoughtworks.androidtrain.utils.getOrAwaitValue
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verifyBlocking

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
        println("[当前before线程为：${Thread.currentThread().name + " " + Thread.currentThread().id}] 0")
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

    @Test
    fun test_viewModel_launch() = runBlocking {
        println("[当前线程为${Thread.currentThread().name + " " + Thread.currentThread().id} 1]")
        val viewModelScope = testViewModel.viewModelScope
        viewModelScope.launch {
            println("[当前线程为${Thread.currentThread().name + " " + Thread.currentThread().id} 7]")
            runBlocking {
                println("[当前线程为${Thread.currentThread().name + " " + Thread.currentThread().id} 9]")
                withContext(Dispatchers.IO) {
                    println("[当前线程为${Thread.currentThread().name + " " + Thread.currentThread().id} 8]")
                    viewModelScope.launch {
                        delay(1000)
                        println("[当前线程为${Thread.currentThread().name + " " + Thread.currentThread().id} 2]")
                    }.join()
                    println("[当前线程为${Thread.currentThread().name + " " + Thread.currentThread().id} 3]")
                }
                println("[当前线程为${Thread.currentThread().name + " " + Thread.currentThread().id} 5]")
            }
            println("[当前线程为${Thread.currentThread().name + " " + Thread.currentThread().id} 4]")
        }.join()
        println("[当前线程为${Thread.currentThread().name + " " + Thread.currentThread().id} 6]")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_suspend_function_thread() = runTest {
        Mockito.`when`(fetchTweetsUseCase.invoke()).thenReturn(emptyList())
        println("[当前线程为${Thread.currentThread().name + " " + Thread.currentThread().id} 1]")
        val viewModelScope = testViewModel.viewModelScope
        println("[当前viewModel外线程为：${Thread.currentThread().name} ${Thread.currentThread().id}] 2")
        viewModelScope.launch {
            fetchTweetsUseCase.invoke()
            println("[当前viewModel内launch线程为：${Thread.currentThread().name} ${Thread.currentThread().id}] 3")
        }
        verifyBlocking(fetchTweetsUseCase) {
            invoke()
        }
        println("[当前线程为${Thread.currentThread().name + " " + Thread.currentThread().id} 4]")
    }
}