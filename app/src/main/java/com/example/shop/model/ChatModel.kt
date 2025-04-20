package com.example.shop.model

data class ChatModel(
    var message: String = "",
    var isUser: Boolean = true,
    var userId: String = "",
    var timestamp: Long = System.currentTimeMillis()
)
