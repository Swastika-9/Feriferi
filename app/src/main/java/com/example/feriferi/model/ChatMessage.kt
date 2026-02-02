package com.example.feriferi.model

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val message: String = "",
    val timestamp: Long = 0,

    // NEW FIELDS
    val type: String = "text",
    val imageUrl: String = ""
)