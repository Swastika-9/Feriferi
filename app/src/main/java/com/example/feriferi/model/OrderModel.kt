package com.example.feriferi.model

data class OrderModel(
    val orderId: String = "",
    val buyerId: String = "",
    val sellerId: String = "",
    val items: List<AddToCartModel> = emptyList(),
    val totalPrice: Double = 0.0,
    val status: String = "Pending", // Pending, Accepted, Delivered
    val timestamp: Long = System.currentTimeMillis(),

    // Delivery Details
    val shippingName: String = "",
    val shippingPhone: String = "",
    val shippingAddress: String = "",
    val paymentMethod: String = "COD" // COD or ESEWA
)