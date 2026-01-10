package com.example.feriferi.model


data class ProductModel(
    val id: String,
    val name: String,
    val price: Double = 0.0,
    val originalPrice: Double? = null,
    val size: String? = null,
    val description: String? = null,
    val color: String? = null,
    val condition: String? = null,
    val quantity: Int? = null,
    val brand: String? = null,
    val tag: String? = null,
    val category: String? = null,
    val gender: String? = null,
    val status: String? = null,
    val imageRes: Int? = null,      // for local testing
    val imageUrl: String? = null    // for Cloudinary
)
