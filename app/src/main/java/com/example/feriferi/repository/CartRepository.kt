package com.example.feriferi.repository

import com.example.feriferi.model.AddToCartModel
import com.example.feriferi.model.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object CartRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()

    // 1. ADD TO CART (Updated to save Brand & Status)
    fun addToCart(product: Item, onResult: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = db.reference.child("Users").child(userId).child("Cart")
        val cartId = cartRef.push().key ?: return

        // Handle image safely
        val img = if (product.imageUrls.isNotEmpty()) product.imageUrls[0] else product.imageUrl

        val item = AddToCartModel(
            id = cartId,
            productId = product.id,
            name = product.name,
            brand = product.brand.ifEmpty { "Unknown" }, // Safety check for empty brand

            // --- FIX IS HERE: Convert Int to Double ---
            price = product.price.toDouble(),
            // ------------------------------------------

            imageUrl = img,
            quantity = 1,
            status = "Accepted",
            sellerId = product.sellerId
        )

        cartRef.child(cartId).setValue(item)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // 2. LISTEN TO ITEMS (Realtime)
    fun getCartItems(onUpdate: (List<AddToCartModel>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = db.reference.child("Users").child(userId).child("Cart")

        cartRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<AddToCartModel>()
                for (child in snapshot.children) {
                    val item = child.getValue(AddToCartModel::class.java)
                    if (item != null) {
                        list.add(item)
                    }
                }
                onUpdate(list)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // 3. UPDATE QUANTITY
    fun updateQuantity(cartId: String, newQty: Int) {
        val userId = auth.currentUser?.uid ?: return
        if (newQty < 1) {
            db.reference.child("Users").child(userId).child("Cart").child(cartId).removeValue()
        } else {
            db.reference.child("Users").child(userId).child("Cart").child(cartId).child("quantity").setValue(newQty)
        }
    }

    // 4. CLEAR CART
    fun clearCart() {
        val userId = auth.currentUser?.uid ?: return
        db.reference.child("Users").child(userId).child("Cart").removeValue()
    }
}