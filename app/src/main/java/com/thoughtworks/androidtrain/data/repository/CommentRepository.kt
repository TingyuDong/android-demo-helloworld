package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Comment
import com.thoughtworks.androidtrain.data.model.Sender
import com.thoughtworks.androidtrain.data.source.local.room.dao.CommentDao
import com.thoughtworks.androidtrain.data.source.local.room.dao.SenderDao
import com.thoughtworks.androidtrain.data.source.local.room.entity.CommentPO
import java.util.stream.Collectors

interface CommentRepositoryInterface {
    suspend fun getComments(tweetId: Int): List<Comment>?
    fun addComments(comments: List<Comment>, tweetId: Int)
    fun addComment(commentPO: CommentPO)
}

class CommentRepository(private val commentDao: CommentDao, private val senderDao: SenderDao) :
    CommentRepositoryInterface {

    private val senderRepository = SenderRepository(senderDao)

    override suspend fun getComments(tweetId: Int): List<Comment>? {
        val commentsPO = commentDao.getComments(tweetId)
        return commentsPO?.map {
            val senderPO = senderDao.getSender(it.senderName) ?: return null
            senderPO.let { it1 ->
                Sender(
                    username = it1.userName,
                    nick = senderPO.nick,
                    avatar = senderPO.avatar
                ).let { sender ->
                    Comment(
                        content = it.content,
                        sender = sender
                    )
                }
            }
        }
    }

    override fun addComments(comments: List<Comment>, tweetId: Int) {
        val commentsCollect = comments.stream().map {
            it.sender.let { sender ->
                senderRepository.addSender(sender)
                CommentPO(
                    id = 0,
                    tweetId = tweetId,
                    content = it.content,
                    senderName = sender.username
                )
            }
        }?.collect(Collectors.toList())
        if (commentsCollect != null) {
            commentDao.insertAllComments(commentsCollect)
        }
    }

    override fun addComment(commentPO: CommentPO) {
        commentDao.insertComment(commentPO)
    }

}