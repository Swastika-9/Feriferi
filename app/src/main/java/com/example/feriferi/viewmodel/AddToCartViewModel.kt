package com.example.feriferi.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.feriferi.model.AddToCartmodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddToCartViewModel : ViewModel() {

    // Cart items state
    var cartItems = mutableStateListOf<AddToCartmodel>()
        private set

    // Add item to cart
    fun addItem(item: AddToCartmodel) {
        val existing = cartItems.find { it.id == item.id }
        if (existing != null) {
            increaseQuantity(existing)
        } else {
            cartItems.add(item)
        }
    }

    // Remove a single item
    fun removeItem(item: AddToCartmodel) {
        cartItems.remove(item)
    }

    // Remove multiple items
    fun removeItems(items: List<AddToCartmodel>) {
        cartItems.removeAll(items)
    }

    // Increase quantity
    fun increaseQuantity(item: AddToCartmodel) {
        val index = cartItems.indexOf(item)
        if (index != -1) {
            val updated = item.copy(quantity = item.quantity + 1)
            cartItems[index] = updated
        }
    }

    // Decrease quantity
    fun decreaseQuantity(item: AddToCartmodel) {
        val index = cartItems.indexOf(item)
        if (index != -1 && item.quantity > 1) {
            val updated = item.copy(quantity = item.quantity - 1)
            cartItems[index] = updated
        }
    }

    // Calculate subtotal
    fun getSubtotal(): Double {
        return cartItems.sumOf { it.price * it.quantity }
    }

    fun getFormattedSubtotal(): String = "Rs %.2f".format(getSubtotal())

    // Delivery fee (example fixed)
    fun getDeliveryFee(): Double = if (cartItems.isEmpty()) 0.0 else 50.0
    fun getFormattedDelivery(): String = "Rs %.2f".format(getDeliveryFee())

    // Total
    fun getTotal(): Double = getSubtotal() + getDeliveryFee()
    fun getFormattedTotal(): String = "Rs %.2f".format(getTotal())

    // Sync cart with Firebase (stub)
    fun syncCartWithFirebase() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val dbRef = FirebaseDatabase.getInstance().getReference("carts/$uid")
        dbRef.setValue(cartItems)
    }
}
