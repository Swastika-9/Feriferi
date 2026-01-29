package com.example.feriferi.repository

import com.example.feriferi.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun registerUser(
        email: String,
        password: String,
        user: UserModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    onFailure(task.exception?.message ?: "Auth failed")
                    return@addOnCompleteListener
                }

                val uid = auth.currentUser?.uid
                if (uid == null) {
                    onFailure("UID is null")
                    return@addOnCompleteListener
                }

                val userData = user.copy(
                    userId = uid,
                    email = email
                )

                database.child("users")
                    .child(uid)
                    .setValue(userData)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onFailure(it.message ?: "Database write failed")
                    }
            }
    }
}
