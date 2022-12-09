package com.thoughtworks.androidtrain.tweets

import androidx.compose.runtime.*

@Composable
fun rememberTweetsState(
    viewModel: TweetsViewModel,
): TweetsState {
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