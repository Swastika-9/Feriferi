package com.example.feriferi.repository

import com.example.feriferi.model.Seller
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SellerRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private val usersRef = db.getReference("Users")

    // --- REALTIME STREAM: This watches the database for changes 24/7 ---
    fun getSellerStream(): Flow<Seller?> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(null) // No user logged in
            close()
            return@callbackFlow
        }

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child("fullName").getValue(String::class.java) ?: "Unknown"
                    val rawUsername = snapshot.child("username").getValue(String::class.java) ?: ""
                    val profileImg = snapshot.child("profileImageUrl").getValue(String::class.java) ?: ""
                    val phone = snapshot.child("phone").getValue(String::class.java) ?: ""

                    // Map to your Seller Model
                    val seller = Seller(
                        id = uid,
                        name = name,
                        username = if (rawUsername.isNotEmpty()) "@$rawUsername" else "@user",
                        profileImageUrl = profileImg,
                        phone = phone,
                        productsSold = 0 // You usually calculate this from the Products list separately
                    )
                    trySend(seller) // Send data to ViewModel
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
                close(error.toException())
            }
        }

        usersRef.child(uid).addValueEventListener(listener)

        awaitClose {
            usersRef.child(uid).removeEventListener(listener)
        }
    }

    // --- UPDATE FUNCTION ---
    fun updateProfile(uid: String, updates: Map<String, Any>, onResult: (Boolean) -> Unit) {
        usersRef.child(uid).updateChildren(updates)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }
}