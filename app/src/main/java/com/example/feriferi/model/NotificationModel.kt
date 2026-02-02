package com.example.feriferi.model

data class NotificationModel(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val timestamp: Long = 0,
    val type: String = "",
    val read: Boolean = false,

    val senderId: String = "",
    val productId: String = "",
    val productImage: String = "",
    val offerPrice: Double = 0.0
)