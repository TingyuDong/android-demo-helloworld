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
                val sender = senderDao.getSender(it.senderName)
                Comment(it.content,
                    sender?.let { it1 ->
                        Sender(
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
            it.sender?.let { it1 ->
                senderRepository.addSender(it1)
                CommentPO(0, tweetId, it.content, it.sender.username)
            }
        }?.collect(Collectors.toList())
        if (commentsCollect != null) {
            commentDao.insertAllComments(commentsCollect)
        }
    }
}