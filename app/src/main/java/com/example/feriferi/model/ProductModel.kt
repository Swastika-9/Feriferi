package com.example.feriferi.model

data class ProductModel(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val originalPrice: Double? = null,
    val quantity: Int = 1,
    val imageUrls: List<String> = emptyList(),
    val category: String = "",
    val subCategory: String = "",
    val size: String? = null,
    val color: String? = null,
    val condition: String? = null,
    val timesWorn: String? = null,
    val brand: String? = null,
    val description: String? = null,
    val tag: String? = null,
    val gender: String? = null
) {

    val status: String
        get() = if (quantity > 0) "Available" else "Sold Out"
}
