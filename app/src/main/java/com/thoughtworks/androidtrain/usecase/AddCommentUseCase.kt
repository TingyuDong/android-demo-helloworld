package com.thoughtworks.androidtrain.usecase

import android.app.Application
import android.content.SharedPreferences
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.repository.*
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

open class AddCommentUseCase(
    private val commentRepository: CommentsRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val application: Application
) {
    suspend operator fun invoke(tweetId: Int, commentContent: String) = withContext(ioDispatcher) {
        val commentPO = CommentPO(
                id = 0,
                tweetId = tweetId,
                content = commentContent,
                senderName = getUserInfo().username
            )
        commentRepository.addComment(commentPO)
    }

    private fun getUserInfo(): Sender {
        val settings: SharedPreferences = application.getSharedPreferences("UserInfo", 0)
        val userName = settings.getString("UserName", "Aiolia")
        val nick = settings.getString("Nick", "Aio")
        val avatar = settings.getString("Avatar", "Avatar.png")
        return Sender(
            username = userName!!,
            nick = nick!!,
            avatar = avatar!!
        )
    }
}