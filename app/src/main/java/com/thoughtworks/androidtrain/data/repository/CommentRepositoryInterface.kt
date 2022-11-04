package com.thoughtworks.androidtrain.data.repository

import com.thoughtworks.androidtrain.data.model.Comment

interface CommentRepositoryInterface {
    fun getComments(tweetId: Int): List<Comment>?
    fun addComments(comments: List<Comment>?, tweetId: Int)
}