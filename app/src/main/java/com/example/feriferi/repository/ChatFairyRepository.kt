package com.example.feriferi.repository

import com.example.feriferi.model.MessageModel

interface ChatFairyRepo {
    fun sendMessage(
        userId: String,
        model: MessageModel,
        callback: (success: Boolean, message: String) -> Unit
    )

    fun getMessages(
        userId: String,
        callback: (success: Boolean, message: String, messages: List<MessageModel>?) -> Unit
    )

    fun uploadImage(
        imageUri: android.net.Uri,
        callback: (success: Boolean, imageUrl: String?) -> Unit
    )
}