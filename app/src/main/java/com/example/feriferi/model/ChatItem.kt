package com.example.feriferi.model

data class ChatItem(
    var chatId: String = "",
    var otherUserId: String = "",
    var otherUserName: String = "",
    var otherUserImage: String = "",
    var lastMessage: String = "",
    var timestamp: Long = 0,
    var unreadCount: Int = 0
) 