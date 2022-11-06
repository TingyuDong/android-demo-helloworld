package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.source.local.room.AppDatabase
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import java.util.stream.Collectors

interface CommentRepositoryInterface {
    fun getComments(tweetId: Int): List<Comment>?
    fun addComments(comments: List<Comment>, tweetId: Int)
}

class CommentRepository : CommentRepositoryInterface {
    private val databaseRepository = DatabaseRepository.get()
    private val database: AppDatabase = databaseRepository.getDatabase()

    private val commentDao = database.commentDao()
    private val senderDao = database.senderDao()

    private val senderRepository = SenderRepository()

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

    override fun addComments(comments: List<Comment>, tweetId: Int) {
        val commentsCollect = comments.stream().map {
            val senderId = it.sender?.let { it1 -> senderRepository.addSender(it1) }
            senderId?.let { it1 -> CommentPO(0, tweetId, it.content, it1.toInt()) }
        }?.collect(Collectors.toList())
        if (commentsCollect != null) {
            commentDao.insertAllComments(commentsCollect)
        }
    }
}