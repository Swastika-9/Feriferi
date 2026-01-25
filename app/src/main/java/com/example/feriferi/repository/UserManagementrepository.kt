package com.example.feriferi.repository

import com.example.feriferi.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun getCurrentUser(
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                if (user != null) onSuccess(user)
            }
            .addOnFailureListener {
                onError(it.message ?: "Error fetching user")
            }
    }

    fun verifyUser(
        uid: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("users")
            .document(uid)
            .update("isVerified", true)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Verification failed") }
    }

    fun removeUser(
        uid: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("users")
            .document(uid)
            .delete()
            .addOnSuccessListener {
                auth.currentUser?.delete()
                onSuccess()
            }
            .addOnFailureListener {
                onError(it.message ?: "Remove failed")
            }
    }

    fun changePassword(
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.currentUser?.updatePassword(newPassword)
            ?.addOnSuccessListener { onSuccess() }
            ?.addOnFailureListener { onError(it.message ?: "Password update failed") }
    }
}