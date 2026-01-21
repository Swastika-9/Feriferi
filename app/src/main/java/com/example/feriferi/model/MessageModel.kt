package com.example.feriferi.model

import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val senderId: String = "",
    val text: String = "",
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isFromBot: Boolean = false,
    val isImageUploading: Boolean = false
)
