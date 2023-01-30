package com.thoughtworks.androidtrain.rule.tweets

import app.cash.turbine.test
import com.thoughtworks.androidtrain.data.Result
import com.thoughtworks.androidtrain.data.Result.Success
import com.thoughtworks.androidtrain.data.Result.Error
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.rule.DispatchersRule
import com.thoughtworks.androidtrain.tweets.TweetsViewModel
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

private const val ERROR_WHILE_LOADING_TASKS = "Error while loading tasks"

@OptIn(ExperimentalCoroutinesApi::class)
class TweetsViewModelTest {
    private lateinit var tweetsViewModel: TweetsViewModel

    private lateinit var fetchTweetsUseCase: FetchTweetsUseCase

    private lateinit var addCommentUseCase: AddCommentUseCase

    private lateinit var addTweetsUseCase: AddTweetUseCase

    private val flow = MutableStateFlow<Result<List<Tweet>>>(Success(
        listOf(
            Tweet(0, "hello", null, null, null, null, null),
            Tweet(0, "hello", null, null, null, null, null)
        )
    ))

    @get:Rule
    val dispatchersRule = DispatchersRule()

    @Before
    fun setupViewModel() {
        fetchTweetsUseCase = Mockito.mock(FetchTweetsUseCase::class.java)
        addCommentUseCase = Mockito.mock(AddCommentUseCase::class.java)
        addTweetsUseCase = Mockito.mock(AddTweetUseCase::class.java)

        `when`(fetchTweetsUseCase.invoke()).thenReturn(flow)
        tweetsViewModel = TweetsViewModel(
            fetchTweetsUseCase = fetchTweetsUseCase,
            addCommentUseCase = addCommentUseCase,
            addTweetUseCase = addTweetsUseCase
        )
    }

    @Test
    fun loadAllTweetsFromUseCase_loadingAndDataLoaded() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        //Given
        `when`(fetchTweetsUseCase.refreshTweets()).then {
            flow.update {
                Success(
                    listOf(
                        Tweet(0, "hello", null, null, null, null, null),
                        Tweet(1, "hello", null, null, null, null, null),
                        Tweet(2, "hello", null, null, null, null, null)
                    )
                )
            }
        }

        //When
        tweetsViewModel.refresh()

        //Then
        tweetsViewModel.uiState.test {
            assertEquals(true, awaitItem().isRefreshing)
        }
        advanceUntilIdle()
        tweetsViewModel.uiState.test {
            assertEquals(false, awaitItem().isRefreshing)
        }
        tweetsViewModel.uiState.test {
            assertEquals(3, awaitItem().tweets.size)
        }
    }

    @Test
    fun loadTweetsError() = runTest {
        //Given
        `when`(fetchTweetsUseCase.refreshTweets()).then {
            flow.update {
                Error(Exception())
            }
        }

        //When
        tweetsViewModel.refresh()

        //Then
        tweetsViewModel.uiState.test {
            assertEquals(false, awaitItem().isRefreshing)
        }
        tweetsViewModel.uiState.test {
            assertEquals(emptyList<Tweet>(), awaitItem().tweets)
        }
        tweetsViewModel.uiState.test {
            assertEquals(ERROR_WHILE_LOADING_TASKS, awaitItem().message)
        }
    }

    @Test
    fun showErrorMessages() = runTest {
        //Given
        //When
        tweetsViewModel.showErrorMessage(ERROR_WHILE_LOADING_TASKS)

        //Then
        tweetsViewModel.uiState.test {
            assertEquals(ERROR_WHILE_LOADING_TASKS, awaitItem().message)
        }
    }
}