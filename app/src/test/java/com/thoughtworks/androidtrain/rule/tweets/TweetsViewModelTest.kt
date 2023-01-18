package com.thoughtworks.androidtrain.rule.tweets

import app.cash.turbine.test
import com.thoughtworks.androidtrain.data.Result
import com.thoughtworks.androidtrain.data.Result.Success
import com.thoughtworks.androidtrain.data.model.Tweet
import com.thoughtworks.androidtrain.rule.DispatchersRule
import com.thoughtworks.androidtrain.tweets.TweetsViewModel
import com.thoughtworks.androidtrain.usecase.AddCommentUseCase
import com.thoughtworks.androidtrain.usecase.AddTweetUseCase
import com.thoughtworks.androidtrain.usecase.FetchTweetsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class TweetsViewModelTest {
    private lateinit var tweetsViewModel: TweetsViewModel

    private lateinit var fetchTweetsUseCase: FetchTweetsUseCase

    private lateinit var addCommentUseCase: AddCommentUseCase

    private lateinit var addTweetsUseCase: AddTweetUseCase

    @get:Rule
    val dispatchersRule = DispatchersRule()

    @Before
    fun setupViewModel() {
        fetchTweetsUseCase = Mockito.mock(FetchTweetsUseCase::class.java)
        addCommentUseCase = Mockito.mock(AddCommentUseCase::class.java)
        addTweetsUseCase = Mockito.mock(AddTweetUseCase::class.java)

        tweetsViewModel = TweetsViewModel(
            fetchTweetsUseCase = fetchTweetsUseCase,
            addCommentUseCase = addCommentUseCase,
            addTweetUseCase = addTweetsUseCase
        )
    }

    @Test
    fun loadAllTweetsFromRepository_loadingAndDataLoaded() = runTest {
        val flow = flowOf<Result<List<Tweet>>>(Success(emptyList()))
        `when`(fetchTweetsUseCase.getTweets()).thenReturn(flow)
        `when`(fetchTweetsUseCase.refreshTweets()).then {
            fetchTweetsUseCase.getTweets()
        }
        tweetsViewModel.refresh()
        tweetsViewModel.uiState.test {
            assertEquals(true, awaitItem().isRefreshing)
        }
//        fetchTweetsUseCase.getTweets()
    }


}