package com.thoughtworks.androidtrain.usecase

import android.content.SharedPreferences
import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.repository.CommentsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

open class AddCommentUseCase(
    private val commentRepository: CommentsRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val sharedPreferences: SharedPreferences
) {
    suspend operator fun invoke(tweetId: Int, commentContent: String) = withContext(ioDispatcher) {
        commentRepository.addComment(
            tweetId = tweetId,
            comment = Comment(content = commentContent, sender = getUserInfo())
        )
    }

    private fun getUserInfo(): Sender {
        val userName = sharedPreferences.getString("UserName", "Aiolia")
        val nick = sharedPreferences.getString("Nick", "Aio")
        val avatar = sharedPreferences.getString("Avatar", "Avatar.png")
        return Sender(
            username = userName!!,
            nick = nick!!,
            avatar = avatar!!
        )
    }
}