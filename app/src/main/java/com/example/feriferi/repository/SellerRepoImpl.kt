package com.example.feriferi.repository

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.feriferi.model.Seller
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SellerRepoImpl {

    // POINT TO REALTIME DATABASE
    private val db = FirebaseDatabase.getInstance()
    private val usersRef = db.getReference("Users")

    fun getSellerData(uid: String, callback: (Boolean, String?, Seller?) -> Unit) {
        usersRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child("fullName").getValue(String::class.java) ?: "Unknown"
                    // Get the username
                    val rawUsername = snapshot.child("username").getValue(String::class.java) ?: ""
                    val profileImg = snapshot.child("profileImageUrl").getValue(String::class.java) ?: ""
                    val phone = snapshot.child("phone").getValue(String::class.java) ?: ""

                    // Convert to Seller Object (Now includes phone and ID)
                    val seller = Seller(
                        id = uid,
                        name = name,
                        username = if (rawUsername.isNotEmpty()) "@$rawUsername" else "@user",
                        profileImageUrl = profileImg,
                        phone = phone // Make sure Seller.kt has this field!
                    )
                    callback(true, "Success", seller)
                } else {
                    callback(false, "User not found", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    // --- UPLOAD IMAGE (Fixed: Returns String) ---
    suspend fun uploadProfileImage(uri: Uri): String = suspendCancellableCoroutine { continuation ->
        MediaManager.get().upload(uri)
            .unsigned("product_images") // Uses your working preset
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    // Return the secure URL as a String
                    continuation.resume(resultData["secure_url"].toString())
                }
                override fun onError(requestId: String, error: ErrorInfo) {
                    continuation.resumeWithException(Exception(error.description))
                }
                override fun onReschedule(requestId: String, error: ErrorInfo) {}
            })
            .dispatch()
    }

    fun updateSellerProfile(uid: String, updates: Map<String, Any>, callback: (Boolean, String) -> Unit) {
        usersRef.child(uid).updateChildren(updates)
            .addOnSuccessListener { callback(true, "Profile Updated") }
            .addOnFailureListener { callback(false, it.message ?: "Error") }
    }
}