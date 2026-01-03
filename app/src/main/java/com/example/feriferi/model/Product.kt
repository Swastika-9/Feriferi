package com.example.feriferi.model

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val originalPrice: Double = 0.0,
    val size: String = "",
    val description: String = "",
    val color: String = "",
    val condition: String = "",
    val timesWorn: Int = 0,
    val company: String = "",
    val tag: String = "",
    val mainCategory: String = "",
    val subCategory: String = "",
    val status: String = "Available",
    val imageUrl: String = ""   // empty for now
)
