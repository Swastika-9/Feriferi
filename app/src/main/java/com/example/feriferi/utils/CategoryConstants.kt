package com.example.feriferi.utils

object CategoryConstants {
    val CATEGORY_MAP = mapOf(
        "Clothing" to listOf("Men's", "Women's", "Kid's", "Sale"),
        "Foot-Wear" to listOf("Sneakers", "Formal", "Sandals", "Boots"),
        "Accessories" to listOf("Bags", "Jewelry", "Watches", "Belts"),
        "Furniture" to listOf("Living Room", "Bedroom", "Office"),
        "Vehicle" to listOf("Car Parts", "Bike Accessories"),
        "Electronics" to listOf("Mobiles", "Laptops", "Gadgets"),
        "Books & Stationary" to listOf("Fiction", "Academic", "Office Supplies")
    )

    fun getMainCategories(): List<String> = CATEGORY_MAP.keys.toList()
}