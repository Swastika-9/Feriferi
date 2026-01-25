package com.example.feriferi.repository

import com.example.feriferi.model.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AddToCartrepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    // Correct syntax for userId getter
    private val userId: String
        get() = auth.currentUser?.uid ?: ""

    // Reference to user's cart in Firebase Realtime Database
    private fun cartRef(): DatabaseReference {
        return database.getReference("carts").child(userId)
    }

    // Save entire cart list to Firebase
    fun syncCart(cartItems: List<CartItem>, onComplete: ((Boolean) -> Unit)? = null) {
        cartRef().setValue(cartItems)
            .addOnSuccessListener { onComplete?.invoke(true) }
            .addOnFailureListener { onComplete?.invoke(false) }
    }

    // Remove specific items from cart in Firebase
    fun removeItems(items: List<CartItem>, onComplete: ((Boolean) -> Unit)? = null) {
        cartRef().get().addOnSuccessListener { snapshot ->
            // Deserialize current cart items
            val currentItems = snapshot.children.mapNotNull { it.getValue(CartItem::class.java) }

            // Filter out items to remove by matching IDs
            val updatedList = currentItems.filter { currentItem ->
                items.none { it.id == currentItem.id }
            }

            // Update Firebase with filtered list
            cartRef().setValue(updatedList)
                .addOnSuccessListener { onComplete?.invoke(true) }
                .addOnFailureListener { onComplete?.invoke(false) }
        }.addOnFailureListener {
            onComplete?.invoke(false)
        }
    }
}
