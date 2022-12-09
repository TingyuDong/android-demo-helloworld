package com.thoughtworks.androidtrain.tweets

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@Composable
fun rememberTweetsState(
    viewModel: TweetsViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
): TweetsState {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.fetchData()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    return remember(viewModel) {
        TweetsState(viewModel)
    }
}

@Stable
class TweetsState(private val viewModel: TweetsViewModel) {
    fun saveComment(): (Int, String, MutableState<Boolean>) -> Unit =
        { tweetId: Int,
          commentContent: String,
          showAddCommentItem: MutableState<Boolean>
            ->
            viewModel.saveComment(tweetId, commentContent)
            showAddCommentItem.value = false
        }
}