package com.example.feriferi.model

data class Item(
    val itemId: String = "",
    val name: String = "",
    val price: Int = 0,
    val originalPrice: Int = 0,
    val sellerName: String = "",
    val soldCount: Int = 0,
    val sizes: List<String> = emptyList(),
    val color: String = "",
    val condition: String = "",
    val purchasedYear: Int = 0,
    val brand: String = "",
    val category: String = "",
    val status: String = "",
    val imageUrl: String = ""
)
