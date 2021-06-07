package com.example.caoApplication

data class ChatDTO(
    var users:HashMap<String?,Boolean> ?=HashMap(),
    var comments:HashMap<String?,Comment>?= HashMap()
){
    data class Comment(
        var uid:String ?=null,
        var message:String ?=null
    )
}
