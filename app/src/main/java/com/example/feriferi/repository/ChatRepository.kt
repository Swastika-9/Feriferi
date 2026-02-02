package com.example.feriferi.repository

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.feriferi.model.ChatItem
import com.example.feriferi.model.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatRepository {
    private val db = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // 1. GENERATE UNIQUE CHAT ID
    fun getChatId(user1: String, user2: String): String {
        return if (user1 < user2) "${user1}_$user2" else "${user2}_$user1"
    }

    // 2. SEND MESSAGE
    fun sendMessage(chatId: String, message: ChatMessage) {
        val ref = db.getReference("Chats").child(chatId).push()
        val msgWithId = message.copy(id = ref.key ?: "")

        ref.setValue(msgWithId).addOnSuccessListener {
            updateLastMessage(chatId, msgWithId)
        }
    }

    // 3. GET INBOX / USER CHATS (FIXES ViewModel Error)
    fun getUserChats(onChats: (List<ChatItem>) -> Unit) {
        val currentUserId = auth.currentUser?.uid ?: return

        db.getReference("Inbox").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ChatItem>()
                for (child in snapshot.children) {
                    val item = child.getValue(ChatItem::class.java)
                    // Only show chats where the current user's ID is part of the chatId
                    if (item != null && item.chatId.contains(currentUserId)) {
                        list.add(item)
                    }
                }
                // Sort by latest message
                onChats(list.sortedByDescending { it.timestamp })
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // 4. UPLOAD TO CLOUDINARY
    fun uploadImage(uri: Uri, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        try {
            MediaManager.get().upload(uri)
                .unsigned("YOUR_UPLOAD_PRESET") // Replace with your preset
                .callback(object : UploadCallback {
                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val url = resultData["secure_url"] as? String ?: ""
                        onSuccess(url)
                    }
                    override fun onError(requestId: String, error: ErrorInfo) {
                        onError(error.description)
                    }
                    override fun onStart(requestId: String) {}
                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                    override fun onReschedule(requestId: String, error: ErrorInfo) {}
                })
                .dispatch()
        } catch (e: Exception) {
            onError("Cloudinary error: ${e.message}")
        }
    }

    // 5. LISTEN FOR MESSAGES
    fun getMessages(chatId: String, onMessages: (List<ChatMessage>) -> Unit) {
        db.getReference("Chats").child(chatId).orderByChild("timestamp")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<ChatMessage>()
                    for (child in snapshot.children) {
                        child.getValue(ChatMessage::class.java)?.let { list.add(it) }
                    }
                    onMessages(list)
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun updateLastMessage(chatId: String, lastMsg: ChatMessage) {
        val lastText = if (lastMsg.type == "image") "Sent a photo" else lastMsg.message
        val updateMap = mapOf(
            "lastMessage" to lastText,
            "timestamp" to lastMsg.timestamp
        )
        db.getReference("Inbox").child(chatId).updateChildren(updateMap)
    }
}