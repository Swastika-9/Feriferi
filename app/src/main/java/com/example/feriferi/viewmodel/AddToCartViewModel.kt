package com.example.feriferi.viewmodel

import androidx.lifecycle.ViewModel
import com.example.feriferi.model.AddToCartModel
import com.example.feriferi.model.OrderModel
import com.example.feriferi.repository.CartRepository
import com.example.feriferi.repository.NotificationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddToCartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<AddToCartModel>>(emptyList())
    val cartItems: StateFlow<List<AddToCartModel>> = _cartItems.asStateFlow()

    // Shipping fee (Fixed for now)
    val shippingFee = 150.0

    init {
        fetchCartItems()
    }

    private fun fetchCartItems() {
        CartRepository.getCartItems { items ->
            _cartItems.value = items
        }
    }

    fun increaseQuantity(item: AddToCartModel) {
        CartRepository.updateQuantity(item.id, item.quantity + 1)
    }

    fun decreaseQuantity(item: AddToCartModel) {
        CartRepository.updateQuantity(item.id, item.quantity - 1)
    }

    // --- CALCULATIONS ---
    fun getSubtotal(): Double {
        return _cartItems.value.sumOf { it.price * it.quantity }
    }

    fun getTotal(): Double {
        return getSubtotal() + shippingFee
    }

    // --- CHECKOUT LOGIC ---
    fun checkout(
        name: String,
        phone: String,
        address: String,
        paymentMethod: String,
        onSuccess: () -> Unit
    ) {
        val items = _cartItems.value
        if (items.isEmpty()) return

        val buyerId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val orderRef = FirebaseDatabase.getInstance().getReference("Orders")

        // Group items by Seller (So each seller gets a separate order)
        val itemsBySeller = items.groupBy { it.sellerId }

        itemsBySeller.forEach { (sellerId, sellerItems) ->
            val orderId = orderRef.push().key ?: return@forEach

            val sellerTotal = sellerItems.sumOf { it.price * it.quantity }

            val order = OrderModel(
                orderId = orderId,
                buyerId = buyerId,
                sellerId = sellerId,
                items = sellerItems,
                totalPrice = sellerTotal,
                status = "Pending",
                timestamp = System.currentTimeMillis(),
                shippingName = name,
                shippingPhone = phone,
                shippingAddress = address,
                paymentMethod = paymentMethod
            )

            orderRef.child(orderId).setValue(order)

            NotificationRepository.sendNotification(
                targetUserId = sellerId,
                title = "New Order Request",
                message = "New order for Rs ${sellerTotal.toInt()}. Tap to Accept/Decline.",
                type = "order_request",

                senderId = buyerId,
                productId = orderId,
                productImage = sellerItems.firstOrNull()?.imageUrl ?: "", // Show the first item's image as a preview
                offerPrice = sellerTotal
            )
        }

        CartRepository.clearCart()
        onSuccess()
    }
}