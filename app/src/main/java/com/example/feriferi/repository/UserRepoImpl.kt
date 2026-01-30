package com.example.feriferi.repository

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.feriferi.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserRepoImpl : UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private val usersRef = db.getReference("Users") // Points to "Users" node
    private val usernamesRef = db.getReference("Usernames")

    // --- REGISTER ---
    override suspend fun registerUser(
        email: String,
        password: String,
        user: UserModel
    ): Pair<Boolean, String> {
        return try {
            val username = user.username.lowercase()

            // 1. Check if username exists (Realtime DB way)
            val usernameSnapshot = usernamesRef.child(username).get().await()
            if (usernameSnapshot.exists()) {
                return Pair(false, "Username already taken")
            }

            // 2. Create Auth User
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: return Pair(false, "Failed to get user ID")

            // 3. Save to Realtime Database
            val userData = user.copy(userId = uid, email = email, username = username)

            usersRef.child(uid).setValue(userData).await()
            usernamesRef.child(username).setValue(uid).await()

            Pair(true, "Registration successful")
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(false, e.message ?: "Registration failed")
        }
    }

    // --- LOGIN ---
    override suspend fun loginUser(email: String, password: String): Pair<Boolean, String> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Pair(true, "Login successful")
        } catch (e: Exception) {
            Pair(false, e.message ?: "Login failed")
        }
    }

    // --- GET CURRENT USER ---
    override suspend fun getCurrentUser(): UserModel? {
        val uid = auth.currentUser?.uid ?: return null
        return try {
            val snapshot = usersRef.child(uid).get().await()
            snapshot.getValue(UserModel::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override fun logout() {
        auth.signOut()
    }

    // --- UPLOAD IMAGE (Cloudinary + Realtime DB Update) ---
    override suspend fun uploadProfileImage(imageUri: Uri): String? {
        return try {
            val uid = auth.currentUser?.uid ?: return null

            // 1. Upload to Cloudinary
            val downloadUrl = uploadToCloudinary(imageUri)

            // 2. Update specific field in Realtime DB immediately
            usersRef.child(uid).child("profileImageUrl").setValue(downloadUrl).await()

            downloadUrl
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // --- HELPER: Cloudinary ---
    private suspend fun uploadToCloudinary(uri: Uri): String = suspendCancellableCoroutine { continuation ->
        MediaManager.get().upload(uri)
            .unsigned("product_images") // Keep this working preset!
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    continuation.resume(resultData["secure_url"].toString())
                }
                override fun onError(requestId: String, error: ErrorInfo) {
                    continuation.resumeWithException(Exception(error.description))
                }
                override fun onReschedule(requestId: String, error: ErrorInfo) {}
            })
            .dispatch()
    }

    // --- USER MANAGEMENT & PROFILE UPDATES ---

    // NEW FUNCTION: Updates specific fields (Name, Phone, ImageUrl) without overwriting the whole user
    override fun updateSellerProfile(uid: String, updates: Map<String, Any>, callback: (Boolean, String) -> Unit) {
        usersRef.child(uid).updateChildren(updates)
            .addOnSuccessListener {
                callback(true, "Profile Updated Successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to update profile")
            }
    }

    override fun verifyUser(uid: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        usersRef.child(uid).child("isVerified").setValue(true)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Error") }
    }

    override fun removeUser(uid: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        usersRef.child(uid).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Error") }
    }

    override fun changePassword(newPassword: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.currentUser?.updatePassword(newPassword)
            ?.addOnSuccessListener { onSuccess() }
            ?.addOnFailureListener { onError(it.message ?: "Error") }
    }
}