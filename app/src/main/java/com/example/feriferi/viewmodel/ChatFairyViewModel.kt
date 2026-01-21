package com.example.feriferi.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feriferi.model.MessageModel
import com.example.feriferi.repository.ChatFairyRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatFairyViewModel(private val repo: ChatFairyRepo) : ViewModel() {

    private val _messages = MutableStateFlow<List<MessageModel>>(emptyList())
    val messages: StateFlow<List<MessageModel>> = _messages

    private val _isBotTyping = MutableStateFlow(false)
    val isBotTyping: StateFlow<Boolean> = _isBotTyping

    fun fetchMessages(userId: String) {
        repo.getMessages(userId) { success, message, data ->
            if (success && data != null) {
                _messages.value = data
            }
        }
    }

    fun handleSendMessage(userId: String, text: String, imageUri: Uri?) {
        if (imageUri != null) {
            // Upload to Cloudinary first
            repo.uploadImage(imageUri) { success, url ->
                if (success) {
                    saveUserMessage(userId, text, url)
                }
            }
        } else {
            saveUserMessage(userId, text, null)
        }
    }

    private fun saveUserMessage(userId: String, text: String, imageUrl: String?) {
        val userMsg = MessageModel(
            senderId = userId,
            text = text,
            imageUrl = imageUrl,
            isFromBot = false,
            timestamp = System.currentTimeMillis()
        )

        repo.sendMessage(userId, userMsg) { success, _ ->
            if (success) {
                triggerBotResponse(userId, text)
            }
        }
    }

    private fun triggerBotResponse(userId: String, userText: String) {
        viewModelScope.launch {
            _isBotTyping.value = true
            delay(1500)

            val botMsg = MessageModel(
                senderId = "CHAT_FAIRY",
                text = "I've analyzed the item! Based on your history, I recommend an XL for a comfortable fit.",
                isFromBot = true,
                timestamp = System.currentTimeMillis()
            )

            repo.sendMessage(userId, botMsg) { _, _ ->
                _isBotTyping.value = false
            }
        }
    }
}