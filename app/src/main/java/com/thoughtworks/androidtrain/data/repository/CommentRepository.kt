package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.source.local.room.dao.CommentDao
import com.thoughtworks.androidtrain.data.source.local.room.dao.SenderDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import java.util.stream.Collectors

interface CommentRepositoryInterface {
    fun getComments(tweetId: Int): List<Comment>?
    fun addComments(comments: List<Comment>, tweetId: Int)
}

class CommentRepository(private val commentDao: CommentDao, private val senderDao: SenderDao) : CommentRepositoryInterface {

    private val senderRepository = SenderRepository(senderDao)

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