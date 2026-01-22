package com.example.feriferi.model

data class MessageModel(
    val messageId: String = java.util.UUID.randomUUID().toString(),
    val senderId: String = "",
    val text: String = "",
    val imageUrl: String? = null,
    val productId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isFromBot: Boolean = false
)