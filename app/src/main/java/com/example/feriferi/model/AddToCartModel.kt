package com.example.feriferi.model

data class AddToCartModel(
    val id: String = "",
    val name: String = "",
    val brand: String = "",
    val price: Double = 0.0,
    val originalPrice: Double = 0.0,
    val quantity: Int = 1,
    val imageUrl: String? = null,
    val sellerId: String = "",
    val status: String = "Pending"
)