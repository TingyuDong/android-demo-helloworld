package com.thoughtworks.androidtrain.model

import com.google.gson.annotations.SerializedName

data class Tweet(
    val id: Int,
    val content: String?,
    val sender: Sender?,
    val images: List<Image>?,
    val comments: List<Comment>?,
    val error: String?,
    @SerializedName("unknown error")
    val unknownError: String?

) {
    override fun toString(): String {
        return "Tweet(content=$content, sender=$sender, images=$images, comments=$comments, error=$error, unknownError=$unknownError)"
    }
}
