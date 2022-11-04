package com.thoughtworks.androidtrain.data.model

data class Sender(
    var id: Int?,
    var username: String,
    var nick: String,
    var avatar: String
){
    fun setId(id: Int){
        this.id = id
    }
}
