package com.example.feriferi.repository

import com.example.feriferi.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserRepoImpl : UserRepo {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    override fun register(
        email: String,
        password: String,
        user: UserModel,
        onResult: (Boolean, String) -> Unit
    ) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    onResult(false, task.exception?.message ?: "Registration failed")
                    return@addOnCompleteListener
                }

                val uid = auth.currentUser?.uid ?: run {
                    onResult(false, "User ID not found")
                    return@addOnCompleteListener
                }

                val userData = user.copy(
                    userId = uid,
                    email = email
                )

                db.child("users")
                    .child(uid)
                    .setValue(userData)
                    .addOnSuccessListener {
                        onResult(true, "Registration successful")
                    }
                    .addOnFailureListener {
                        onResult(false, it.message ?: "Database error")
                    }
            }
    }

    override fun getCurrentUser(
        onSuccess: (UserModel) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: return

        db.child("users")
            .child(uid)
            .get()
            .addOnSuccessListener {
                val user = it.getValue(UserModel::class.java)
                if (user != null) onSuccess(user)
                else onFailure("User not found")
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Error fetching user")
            }
    }
}
