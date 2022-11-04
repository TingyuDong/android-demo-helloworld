package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import java.util.stream.Collectors

class CommentRepository(repository: Repository) : CommentRepositoryInterface {
    private val database: AppDatabase = repository.getDatabase()

    private val commentDao = database.commentDao()
    private val senderDao = database.senderDao()

    private val senderRepository = SenderRepository(repository)

    override fun getComments(tweetId: Int): List<Comment>? {
        val commentsPO = commentDao.getComments(tweetId)
        if (commentsPO != null) {
            return commentsPO.stream().map {
                val sender = senderDao.getSender(it.senderId)
                Comment(it.content,
                    sender?.let { it1 ->
                        Sender(
                            it1.id,
                            it1.userName,
                            sender.nick,
                            sender.avatar
                        )
                    })
            }.collect(Collectors.toList())
        }
        return null
    }

    override fun addComments(comments: List<Comment>?, tweetId: Int) {
        val commentsCollect = comments?.stream()?.map {
            val senderId = senderRepository.addSender(it.sender)
            senderId?.let { it1 -> CommentPO(0, tweetId, it.content, it1.toInt()) }
        }?.collect(Collectors.toList())
        if (commentsCollect != null) {
            commentDao.insertAllComments(commentsCollect)
        }
    }
}