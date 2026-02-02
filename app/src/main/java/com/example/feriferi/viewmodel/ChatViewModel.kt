package com.example.feriferi.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.feriferi.model.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class ChatViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private var listenerAttached = false

    fun loadMessages(chatId: String) {
        if (listenerAttached) return
        listenerAttached = true

        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                _messages.value =
                    snapshot?.toObjects(ChatMessage::class.java) ?: emptyList()
            }
    }

    fun sendMessage(chatId: String, otherUserId: String, otherUserName: String, text: String) {
        val ref = firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .document()

        ref.set(
            ChatMessage(
                id = ref.id,
                senderId = userId,
                message = text,
                timestamp = System.currentTimeMillis(),
                type = "text"
            )
        )
    }

    fun uploadChatImage(
        uri: Uri,
        chatId: String,
        callback: (Boolean) -> Unit
    ) {
        val imageRef =
            storage.reference.child("chat_images/${UUID.randomUUID()}.jpg")

        imageRef.putFile(uri)
            .continueWithTask { imageRef.downloadUrl }
            .addOnSuccessListener { url ->
                val ref = firestore.collection("chats")
                    .document(chatId)
                    .collection("messages")
                    .document()

                ref.set(
                    ChatMessage(
                        id = ref.id,
                        senderId = userId,
                        message = "Sent a photo",
                        timestamp = System.currentTimeMillis(),
                        imageUrl = url.toString(),
                        type = "image"
                    )
                ).addOnSuccessListener { callback(true) }
                    .addOnFailureListener { callback(false) }
            }
            .addOnFailureListener { callback(false) }
    }

    override fun onCleared() {
        super.onCleared()
        listenerAttached = false
    }
}