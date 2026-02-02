package com.example.feriferi.model

data class AddToCartModel(
    val id: String = "",
    val productId: String = "",
    val name: String = "",
    val brand: String = "",
    val price: Double = 0.0,
    val imageUrl: String? = null,
    val quantity: Int = 1,
    val status: String = "Accepted",
    val sellerId: String = ""
)