package com.thoughtworks.androidtrain.tweets

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberTweetsState(
    viewModel: TweetsViewModel,
    context: Context = LocalContext.current,
): TweetsState {
    return remember(viewModel, context) {
        TweetsState(viewModel, context)
    }
}

@Stable
class TweetsState(
    private val viewModel: TweetsViewModel,
    private val context: Context
) {
    fun saveComment(): (Int, String, MutableState<Boolean>) -> Unit =
        { tweetId: Int,
          commentContent: String,
          showAddCommentItem: MutableState<Boolean>
            ->
            viewModel.saveComment(tweetId, commentContent)
            showAddCommentItem.value = false
        }

    fun showMessage(message: String) {
        if (message.isNotEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.cleanMessage()
        }
    }
}