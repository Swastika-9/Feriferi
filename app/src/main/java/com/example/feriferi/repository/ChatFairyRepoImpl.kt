package com.example.feriferi.repository

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.feriferi.model.MessageModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatFairyRepoImpl : ChatFairyRepo {

    private val firestore = FirebaseFirestore.getInstance()
    private val chatRef = firestore.collection("chat_fairy_sessions")

    override fun sendMessage(
        userId: String,
        model: MessageModel,
        callback: (Boolean, String) -> Unit
    ) {
        // Create a reference inside the user's specific chat session
        val doc = chatRef.document(userId).collection("messages").document()

        // Copy the model with the auto-generated Firestore ID (similar to your Product logic)
        val newMessage = model.copy(messageId = doc.id)

        doc.set(newMessage)
            .addOnSuccessListener {
                callback(true, "Message sent successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to send message")
            }
    }

    override fun getMessages(
        userId: String,
        callback: (Boolean, String, List<MessageModel>?) -> Unit
    ) {
        // Using addSnapshotListener for real-time updates so ChatFairy feels "live"
        chatRef.document(userId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    callback(false, error.message ?: "Error loading messages", null)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull {
                        it.toObject(MessageModel::class.java)
                    }
                    callback(true, "Messages updated", messages)
                }
            }
    }

    override fun uploadImage(
        imageUri: Uri,
        callback: (Boolean, String?) -> Unit
    ) {
        // Perfect for the paperclip icon in your PheriBot UI
        MediaManager.get().upload(imageUri)
            .unsigned("your_unsigned_preset") // Replace with your Cloudinary preset
            .callback(object : UploadCallback {
                override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                    val url = resultData?.get("secure_url") as? String
                    callback(true, url)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    callback(false, error?.description ?: "Image upload failed")
                }

                override fun onStart(requestId: String?) {}
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            }).dispatch()
    }
}